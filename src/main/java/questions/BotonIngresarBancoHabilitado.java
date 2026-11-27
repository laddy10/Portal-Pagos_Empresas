package questions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import userinterfaces.PaginaBancoPopularPse;

public class BotonIngresarBancoHabilitado implements Question<Boolean> {

  public static BotonIngresarBancoHabilitado correctamente() {
    return new BotonIngresarBancoHabilitado();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    WebElementFacade boton = PaginaBancoPopularPse.BOTON_INGRESAR.resolveFor(actor);
    return boton.isVisible() && boton.isEnabled();
  }
}
