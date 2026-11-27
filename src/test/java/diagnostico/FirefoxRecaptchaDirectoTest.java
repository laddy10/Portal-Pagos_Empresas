package diagnostico;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.util.List;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirefoxRecaptchaDirectoTest {

  private static final String PORTAL_PAGOS_PASSWORD = "PORTAL_PAGOS_PASSWORD";
  private static final String FIREFOX_BINARY = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
  private static final String URL_PORTAL_PRE =
      "https://empresaspre.portalpagospre.claro.com.co/phrame.php?action=despliegue_personal"
          + "&clase=vistasclaro&metodo=pantalla_inicio&empresa=claro&segmento=empresas";
  private static final String NUMERO_CUENTA = "56220783";

  private static final int TIEMPO_ESPERA_PORTAL_SEGUNDOS = 10;
  private static final int TIEMPO_ESPERA_RECAPTCHA_SEGUNDOS = 15;

  private static final By MODAL_INFORMACION_IMPORTANTE = By.cssSelector(".modal-dialog.modal-info");
  private static final By BOTON_ENTENDIDO =
      By.cssSelector("input.class-boton-conf[value='Entendido']");
  private static final By SELECTOR_DE_SERVICIO = By.cssSelector("div#select.select");
  private static final By CONTENEDOR_DE_OPCIONES = By.cssSelector("div#opciones.opciones");
  private static final By OPCION_PAGO_SOLUCIONES_FIJAS =
      By.cssSelector("#opciones a.opcion[data-target='#10001']");
  private static final By PAGO_SOLUCIONES_FIJAS_SELECCIONADO =
      By.cssSelector("#select [data-codigo='10001']");
  private static final By CAMPO_NUMERO_CUENTA = By.cssSelector("#newReference");

  private static final By IFRAME_RECAPTCHA =
      By.cssSelector("iframe[title='reCAPTCHA'][src*='/recaptcha/api2/anchor']");
  private static final By CHECKBOX_RECAPTCHA = By.cssSelector("#recaptcha-anchor");
  private static final By IFRAME_DESAFIO = By.cssSelector("iframe[src*='/recaptcha/api2/bframe']");

  @Test
  public void comprobarRecaptchaDirectamenteConFirefox() {
    String clave = obtenerClaveDesdeVariableDeEntorno();
    WebDriver driver = null;

    try {
      WebDriverManager.firefoxdriver().setup();

      FirefoxOptions options = new FirefoxOptions();
      options.setBinary(new FirefoxBinary(new File(FIREFOX_BINARY)));

      driver = new FirefoxDriver(options);
      WebDriverWait esperaPortal = new WebDriverWait(driver, TIEMPO_ESPERA_PORTAL_SEGUNDOS);

      driver.get(URL_PORTAL_PRE);

      Alert prompt = esperaPortal.until(ExpectedConditions.alertIsPresent());
      prompt.sendKeys(clave);
      prompt.accept();

      esperaPortal.until(ExpectedConditions.visibilityOfElementLocated(SELECTOR_DE_SERVICIO));
      cerrarModalInformacionImportante(driver, esperaPortal);
      seleccionarPagoSolucionesFijas(driver, esperaPortal);
      ingresarNumeroCuenta(driver, esperaPortal);

      ResultadoRecaptcha resultado = probarClicNormalEnRecaptcha(driver);
      System.out.println("RECAPTCHA_FIREFOX_RESULTADO=" + resultado.name());
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  private String obtenerClaveDesdeVariableDeEntorno() {
    String clave = System.getenv(PORTAL_PAGOS_PASSWORD);

    if (clave == null || clave.trim().isEmpty()) {
      throw new IllegalStateException("No está configurada la variable " + PORTAL_PAGOS_PASSWORD);
    }

    return clave;
  }

  private void cerrarModalInformacionImportante(WebDriver driver, WebDriverWait esperaPortal) {
    esperaPortal.until(ExpectedConditions.visibilityOfElementLocated(MODAL_INFORMACION_IMPORTANTE));
    esperaPortal.until(ExpectedConditions.elementToBeClickable(BOTON_ENTENDIDO)).click();
    esperaPortal.until(
        ExpectedConditions.invisibilityOfElementLocated(MODAL_INFORMACION_IMPORTANTE));
  }

  private void seleccionarPagoSolucionesFijas(WebDriver driver, WebDriverWait esperaPortal) {
    esperaPortal.until(ExpectedConditions.elementToBeClickable(SELECTOR_DE_SERVICIO)).click();
    esperaPortal.until(ExpectedConditions.visibilityOfElementLocated(CONTENEDOR_DE_OPCIONES));
    esperaPortal
        .until(ExpectedConditions.elementToBeClickable(OPCION_PAGO_SOLUCIONES_FIJAS))
        .click();
    esperaPortal.until(
        ExpectedConditions.visibilityOfElementLocated(PAGO_SOLUCIONES_FIJAS_SELECCIONADO));
    esperaPortal.until(ExpectedConditions.invisibilityOfElementLocated(CONTENEDOR_DE_OPCIONES));
  }

  private void ingresarNumeroCuenta(WebDriver driver, WebDriverWait esperaPortal) {
    WebElement campoNumeroCuenta =
        esperaPortal.until(ExpectedConditions.visibilityOfElementLocated(CAMPO_NUMERO_CUENTA));
    campoNumeroCuenta.clear();
    campoNumeroCuenta.sendKeys(NUMERO_CUENTA);
  }

  private ResultadoRecaptcha probarClicNormalEnRecaptcha(WebDriver driver) {
    WebDriverWait esperaRecaptcha = new WebDriverWait(driver, TIEMPO_ESPERA_RECAPTCHA_SEGUNDOS);

    try {
      WebElement iframe =
          esperaRecaptcha.until(ExpectedConditions.presenceOfElementLocated(IFRAME_RECAPTCHA));
      driver.switchTo().frame(iframe);

      esperaRecaptcha.until(ExpectedConditions.elementToBeClickable(CHECKBOX_RECAPTCHA)).click();

      return esperaRecaptcha.until(
          navegador -> {
            navegador.switchTo().defaultContent();

            if (hayDesafioVisible(navegador)) {
              return ResultadoRecaptcha.CHALLENGE;
            }

            WebElement iframeCheckbox = navegador.findElement(IFRAME_RECAPTCHA);
            navegador.switchTo().frame(iframeCheckbox);

            String ariaChecked =
                navegador.findElement(CHECKBOX_RECAPTCHA).getAttribute("aria-checked");

            return "true".equalsIgnoreCase(ariaChecked) ? ResultadoRecaptcha.APROBADO : null;
          });
    } catch (TimeoutException exception) {
      throw new IllegalStateException(
          "reCAPTCHA no fue aprobado ni mostró un desafío en Firefox dentro de "
              + TIEMPO_ESPERA_RECAPTCHA_SEGUNDOS
              + " segundos.",
          exception);
    } finally {
      driver.switchTo().defaultContent();
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
    CHALLENGE
  }
}
