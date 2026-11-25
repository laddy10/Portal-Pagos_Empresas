package interactions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Hace clic en el checkbox del reCAPTCHA. Si queda aprobado directamente (sesión con confianza
 * suficiente) continúa. Si reCAPTCHA pide el desafío de imágenes y {@code -Drecaptcha.manual=true}
 * (valor por defecto), pausa la automatización para que una persona lo resuelva en la ventana del
 * navegador y continúa cuando el checkbox queda marcado.
 *
 * <p>La automatización nunca intenta resolver el desafío por sí misma.
 */
public class ResolverRecaptcha implements Interaction {

  public static final String RECAPTCHA_APROBADO = "recaptchaAprobado";
  public static final String RECAPTCHA_REQUIRIO_DESAFIO = "recaptchaRequirioDesafio";

  private static final int TIEMPO_ESPERA_SEGUNDOS = 15;
  private static final By IFRAME_RECAPTCHA =
      By.cssSelector("iframe[title='reCAPTCHA'][src*='/recaptcha/api2/anchor']");
  private static final By CHECKBOX_RECAPTCHA = By.cssSelector("#recaptcha-anchor");
  private static final By IFRAME_DESAFIO = By.cssSelector("iframe[src*='/recaptcha/api2/bframe']");

  public static ResolverRecaptcha enElCheckbox() {
    return Tasks.instrumented(ResolverRecaptcha.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    actor.remember(RECAPTCHA_APROBADO, false);
    actor.remember(RECAPTCHA_REQUIRIO_DESAFIO, false);

    try {
      WebDriverWait espera = new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS);
      WebElement iframe =
          espera.until(ExpectedConditions.presenceOfElementLocated(IFRAME_RECAPTCHA));
      driver.switchTo().frame(iframe);
      espera.until(ExpectedConditions.elementToBeClickable(CHECKBOX_RECAPTCHA)).click();
      driver.switchTo().defaultContent();

      Resultado resultado;
      try {
        resultado = esperarResultado(driver, TIEMPO_ESPERA_SEGUNDOS, true);
      } catch (IllegalStateException sinRespuesta) {
        // Ni aprobado ni desafío visible (p.ej. el desafío aún cargando): se trata como desafío.
        resultado = Resultado.SIN_RESPUESTA;
      }

      if (resultado != Resultado.APROBADO) {
        actor.remember(RECAPTCHA_REQUIRIO_DESAFIO, true);
        System.out.println("RECAPTCHA_RESULTADO=" + resultado.name());
        guardarCaptura(driver, "recaptcha-" + resultado.name().toLowerCase());

        if (!modoManualActivo()) {
          throw new IllegalStateException(
              "reCAPTCHA solicitó un desafío adicional y recaptcha.manual=false.");
        }

        int segundos = tiempoManualSegundos();
        System.out.println(
            "\n================================================================\n"
                + " reCAPTCHA pidió desafío. Resuélvalo MANUALMENTE en el navegador.\n"
                + " Esperando hasta "
                + segundos
                + " segundos...\n"
                + "================================================================\n");
        resultado = esperarResultado(driver, segundos, false);
      } else {
        System.out.println("RECAPTCHA_RESULTADO=APROBADO_SIN_DESAFIO");
      }

      actor.remember(RECAPTCHA_APROBADO, resultado == Resultado.APROBADO);
    } finally {
      driver.switchTo().defaultContent();
    }
  }

  private Resultado esperarResultado(WebDriver driver, int segundos, boolean detectarDesafio) {
    try {
      return new WebDriverWait(driver, segundos, 500)
          .until(
              navegador -> {
                navegador.switchTo().defaultContent();

                if (detectarDesafio && hayDesafioVisible(navegador)) {
                  return Resultado.DESAFIO;
                }

                navegador.switchTo().frame(navegador.findElement(IFRAME_RECAPTCHA));
                String ariaChecked =
                    navegador.findElement(CHECKBOX_RECAPTCHA).getAttribute("aria-checked");
                navegador.switchTo().defaultContent();

                return "true".equalsIgnoreCase(ariaChecked) ? Resultado.APROBADO : null;
              });
    } catch (TimeoutException exception) {
      throw new IllegalStateException(
          "reCAPTCHA no quedó aprobado dentro de " + segundos + " segundos.", exception);
    }
  }

  private static void guardarCaptura(WebDriver driver, String nombre) {
    try {
      driver.switchTo().defaultContent();
      byte[] imagen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      Path destino =
          Paths.get(System.getProperty("user.dir"), "target", "recaptcha", nombre + ".png");
      Files.createDirectories(destino.getParent());
      Files.write(destino, imagen);
      System.out.println("RECAPTCHA_CAPTURA=" + destino.toAbsolutePath());
    } catch (Exception exception) {
      System.out.println("RECAPTCHA_CAPTURA=ERROR " + exception.getMessage());
    }
  }

  private boolean hayDesafioVisible(WebDriver driver) {
    List<WebElement> iframes = driver.findElements(IFRAME_DESAFIO);
    for (WebElement iframe : iframes) {
      if (iframe.isDisplayed()) {
        return true;
      }
    }
    return false;
  }

  private static boolean modoManualActivo() {
    return Boolean.parseBoolean(System.getProperty("recaptcha.manual", "true"));
  }

  private static int tiempoManualSegundos() {
    try {
      return Integer.parseInt(System.getProperty("recaptcha.manual.segundos", "180"));
    } catch (NumberFormatException exception) {
      return 180;
    }
  }

  private enum Resultado {
    APROBADO,
    DESAFIO,
    SIN_RESPUESTA
  }
}
