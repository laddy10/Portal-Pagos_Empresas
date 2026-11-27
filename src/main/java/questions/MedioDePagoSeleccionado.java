package questions;

import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import tasks.SeleccionarMedioDePago;
import userinterfaces.PaginaPortalPagosEmpresas;

public class MedioDePagoSeleccionado implements Question<Boolean> {

  private final String medioDePagoEsperado;

  public MedioDePagoSeleccionado(String medioDePagoEsperado) {
    this.medioDePagoEsperado = medioDePagoEsperado;
  }

  public static MedioDePagoSeleccionado es(String medioDePagoEsperado) {
    return new MedioDePagoSeleccionado(medioDePagoEsperado);
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    if (SeleccionarMedioDePago.esOpcionPseNequiDaviplata(medioDePagoEsperado)) {
      boolean logoPseVisible =
          PaginaPortalPagosEmpresas.MEDIO_PAGO_PSE_SELECCIONADO.resolveFor(actor).isVisible();
      String valorFormaPago =
          PaginaPortalPagosEmpresas.INPUT_FORMA_PAGO.resolveFor(actor).getAttribute("value");
      return logoPseVisible && valorFormaPago != null && !valorFormaPago.trim().isEmpty();
    }

    WebDriver driver = BrowseTheWeb.as(actor).getDriver();

    List<WebElement> selectoresClaro = driver.findElements(By.cssSelector("#select"));
    for (WebElement selector : selectoresClaro) {
      if (selector.isDisplayed()
          && SeleccionarMedioDePago.coincideMedioDePago(selector.getText(), medioDePagoEsperado)) {
        return true;
      }
    }

    List<WebElement> selectsNativos = driver.findElements(By.tagName("select"));
    for (WebElement selectElement : selectsNativos) {
      if (selectElement.isDisplayed()) {
        String seleccionado = new Select(selectElement).getFirstSelectedOption().getText();
        if (SeleccionarMedioDePago.coincideMedioDePago(seleccionado, medioDePagoEsperado)) {
          return true;
        }
      }
    }

    return false;
  }
}
