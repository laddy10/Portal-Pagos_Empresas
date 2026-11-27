package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import userinterfaces.PaginaFormularioPagoPse;

public class FormularioPagoPseCargo implements Question<Boolean> {

  public static FormularioPagoPseCargo correctamente() {
    return new FormularioPagoPseCargo();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    String urlActual = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
    boolean urlPagos = urlActual != null && urlActual.toLowerCase().contains("pagos.php");
    boolean selectorBancoVisible =
        PaginaFormularioPagoPse.SELECT_BANCO.resolveFor(actor).isVisible();

    return urlPagos && selectorBancoVisible;
  }
}
