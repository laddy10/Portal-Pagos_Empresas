package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import userinterfaces.PaginaPortalPagosEmpresas;

public class PortalClaroEmpresasCargo implements Question<Boolean> {

  public static PortalClaroEmpresasCargo correctamente() {
    return new PortalClaroEmpresasCargo();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    return PaginaPortalPagosEmpresas.SELECTOR_DE_SERVICIO.resolveFor(actor).isVisible();
  }
}
