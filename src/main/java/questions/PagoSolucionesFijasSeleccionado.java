package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import userinterfaces.PaginaPortalPagosEmpresas;

public class PagoSolucionesFijasSeleccionado implements Question<Boolean> {

  public static PagoSolucionesFijasSeleccionado correctamente() {
    return new PagoSolucionesFijasSeleccionado();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    boolean opcionSeleccionada =
        PaginaPortalPagosEmpresas.PAGO_SOLUCIONES_FIJAS_SELECCIONADO.resolveFor(actor).isVisible();
    boolean opcionesOcultas =
        !PaginaPortalPagosEmpresas.CONTENEDOR_DE_OPCIONES.resolveFor(actor).isVisible();

    return opcionSeleccionada && opcionesOcultas;
  }
}
