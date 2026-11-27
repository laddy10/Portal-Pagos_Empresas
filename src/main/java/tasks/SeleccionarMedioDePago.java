package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import userinterfaces.PaginaPortalPagosEmpresas;

public class SeleccionarMedioDePago implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 15;
  private static final By OPCIONES_DROPDOWN_CLARO =
      By.cssSelector(
          "#opciones a.opcion, #opciones .opcion, .opciones a.opcion, .opciones .opcion");
  private static final By OPCIONES_GENERICAS =
      By.xpath(
          "//a[normalize-space(.)!=''] | //li[normalize-space(.)!='']"
              + " | //button[normalize-space(.)!=''] | //div[@role='option']");

  private final String medioDePago;

  public SeleccionarMedioDePago(String medioDePago) {
    this.medioDePago = medioDePago;
  }

  public static SeleccionarMedioDePago conNombre(String medioDePago) {
    return Tasks.instrumented(SeleccionarMedioDePago.class, medioDePago);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();

    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.SELECTOR_MEDIO_DE_PAGO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Scroll.to(PaginaPortalPagosEmpresas.SELECTOR_MEDIO_DE_PAGO));

    WebElement selector = PaginaPortalPagosEmpresas.SELECTOR_MEDIO_DE_PAGO.resolveFor(actor);
    if ("select".equalsIgnoreCase(selector.getTagName())) {
      seleccionarEnSelectNativo(selector, medioDePago);
      actor.attemptsTo(CapturarEvidencia.titulada("Selección del medio de pago " + medioDePago));
      return;
    }

    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.SELECTOR_MEDIO_DE_PAGO, isClickable())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Click.on(PaginaPortalPagosEmpresas.SELECTOR_MEDIO_DE_PAGO),
        CapturarEvidencia.titulada("Apertura del selector de medios de pago"));

    if (esOpcionPseNequiDaviplata(medioDePago)) {
      actor.attemptsTo(
          WaitUntil.the(
                  PaginaPortalPagosEmpresas.OPCION_MEDIO_PAGO_PSE_NEQUI_DAVIPLATA, isVisible())
              .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
              .seconds(),
          Scroll.to(PaginaPortalPagosEmpresas.OPCION_MEDIO_PAGO_PSE_NEQUI_DAVIPLATA),
          WaitUntil.the(
                  PaginaPortalPagosEmpresas.OPCION_MEDIO_PAGO_PSE_NEQUI_DAVIPLATA, isClickable())
              .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
              .seconds(),
          Click.on(PaginaPortalPagosEmpresas.OPCION_MEDIO_PAGO_PSE_NEQUI_DAVIPLATA),
          WaitUntil.the(PaginaPortalPagosEmpresas.MEDIO_PAGO_PSE_SELECCIONADO, isVisible())
              .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
              .seconds(),
          CapturarEvidencia.titulada("Selección del medio de pago " + medioDePago));
      return;
    }

    WebElement opcion = esperarOpcionVisible(driver, medioDePago);
    ((JavascriptExecutor) driver)
        .executeScript("arguments[0].scrollIntoView({block: 'center'});", opcion);
    try {
      opcion.click();
    } catch (Exception exception) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opcion);
    }

    actor.attemptsTo(CapturarEvidencia.titulada("Selección del medio de pago " + medioDePago));
  }

  private static void seleccionarEnSelectNativo(WebElement selectElement, String objetivo) {
    Select select = new Select(selectElement);
    for (WebElement option : select.getOptions()) {
      if (coincideMedioDePago(option.getText(), objetivo)) {
        option.click();
        return;
      }
    }
    throw new IllegalStateException(
        "No se encontró el medio de pago '" + objetivo + "' en el selector <select>.");
  }

  private static WebElement esperarOpcionVisible(WebDriver driver, String objetivo) {
    try {
      return new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS)
          .until(
              navegador -> {
                WebElement enClaro =
                    buscarCoincidencia(navegador.findElements(OPCIONES_DROPDOWN_CLARO), objetivo);
                if (enClaro != null) {
                  return enClaro;
                }
                return buscarCoincidencia(navegador.findElements(OPCIONES_GENERICAS), objetivo);
              });
    } catch (Exception exception) {
      List<String> disponibles = new ArrayList<>();
      for (WebElement elemento : driver.findElements(OPCIONES_DROPDOWN_CLARO)) {
        if (elemento.isDisplayed()) {
          disponibles.add(elemento.getText().replaceAll("\\s+", " ").trim());
        }
      }
      throw new IllegalStateException(
          "No se encontró visible el medio de pago '"
              + objetivo
              + "'. Opciones visibles detectadas: "
              + disponibles,
          exception);
    }
  }

  private static WebElement buscarCoincidencia(List<WebElement> elementos, String objetivo) {
    for (WebElement elemento : elementos) {
      try {
        if (elemento.isDisplayed() && coincideMedioDePago(elemento.getText(), objetivo)) {
          return elemento;
        }
      } catch (Exception ignorado) {
        // Ignorar elementos obsoletos durante el renderizado
      }
    }
    return null;
  }

  public static boolean esOpcionPseNequiDaviplata(String objetivo) {
    String norm = normalizar(objetivo);
    return norm.contains("pse") || (norm.contains("nequi") && norm.contains("daviplata"));
  }

  public static boolean coincideMedioDePago(String textoActual, String objetivo) {
    String actualNorm = normalizar(textoActual);
    String objetivoNorm = normalizar(objetivo);
    if (actualNorm.isEmpty() || objetivoNorm.isEmpty()) {
      return false;
    }
    if (actualNorm.contains(objetivoNorm)) {
      return true;
    }
    // Permite coincidir si el portal usa otro separador (ej. "/", espacio o guion)
    String[] palabrasClave = objetivoNorm.split("[^a-z0-9]+");
    int validas = 0;
    for (String palabra : palabrasClave) {
      if (palabra.length() >= 2) {
        validas++;
        if (!actualNorm.contains(palabra)) {
          return false;
        }
      }
    }
    return validas > 0;
  }

  private static String normalizar(String texto) {
    if (texto == null) {
      return "";
    }
    String sinTildes =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    String guionesUnificados = sinTildes.replaceAll("[\\u2010-\\u2015\\u2212]", "-");
    return guionesUnificados.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
