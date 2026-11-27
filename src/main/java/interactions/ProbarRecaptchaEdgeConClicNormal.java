package interactions;

import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProbarRecaptchaEdgeConClicNormal implements Interaction {

  public static final String RECAPTCHA_EDGE_APROBADO = "recaptchaEdgeAprobado";

  private static final int TIEMPO_ESPERA_SEGUNDOS = 15;
  private static final By IFRAME_RECAPTCHA =
      By.cssSelector("iframe[title='reCAPTCHA'][src*='/recaptcha/api2/anchor']");
  private static final By CHECKBOX_RECAPTCHA = By.cssSelector("#recaptcha-anchor");
  private static final By IFRAME_DESAFIO = By.cssSelector("iframe[src*='/recaptcha/api2/bframe']");

  public static ProbarRecaptchaEdgeConClicNormal enElCheckbox() {
    return Tasks.instrumented(ProbarRecaptchaEdgeConClicNormal.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    WebDriverWait espera = new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS);

    actor.remember(RECAPTCHA_EDGE_APROBADO, false);

    try {
      WebElement iframe =
          espera.until(ExpectedConditions.presenceOfElementLocated(IFRAME_RECAPTCHA));
      driver.switchTo().frame(iframe);

      WebElement checkbox =
          espera.until(ExpectedConditions.elementToBeClickable(CHECKBOX_RECAPTCHA));
      checkbox.click();

      ResultadoRecaptcha resultado = esperarResultado(driver);

      if (resultado == ResultadoRecaptcha.DESAFIO_ADICIONAL) {
        throw new IllegalStateException(
            "reCAPTCHA solicitó un desafío adicional en Edge. "
                + "La automatización no intentará resolverlo.");
      }

      actor.remember(RECAPTCHA_EDGE_APROBADO, true);
    } finally {
      driver.switchTo().defaultContent();
    }
  }

  private ResultadoRecaptcha esperarResultado(WebDriver driver) {
    try {
      return new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS)
          .until(
              navegador -> {
                navegador.switchTo().defaultContent();

                if (hayDesafioVisible(navegador)) {
                  return ResultadoRecaptcha.DESAFIO_ADICIONAL;
                }

                WebElement iframe = navegador.findElement(IFRAME_RECAPTCHA);
                navegador.switchTo().frame(iframe);

                String ariaChecked =
                    navegador.findElement(CHECKBOX_RECAPTCHA).getAttribute("aria-checked");

                return "true".equalsIgnoreCase(ariaChecked) ? ResultadoRecaptcha.APROBADO : null;
              });
    } catch (TimeoutException exception) {
      throw new IllegalStateException(
          "reCAPTCHA no fue aprobado ni solicitó un desafío adicional en Edge dentro de "
              + TIEMPO_ESPERA_SEGUNDOS
              + " segundos.",
          exception);
    }
  }

  private boolean hayDesafioVisible(WebDriver driver) {
    List<WebElement> iframesDeDesafio = driver.findElements(IFRAME_DESAFIO);

    for (WebElement iframe : iframesDeDesafio) {
      if (iframe.isDisplayed()) {
        return true;
      }
    }

    return false;
  }

  private enum ResultadoRecaptcha {
    APROBADO,
    DESAFIO_ADICIONAL
  }
}
