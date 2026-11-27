package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import userinterfaces.PaginaPortalPagosEmpresas;

public class CuentaFueAgregada implements Question<Boolean> {

  private final String numeroCuenta;

  public CuentaFueAgregada(String numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

  public static CuentaFueAgregada conNumero(String numeroCuenta) {
    return new CuentaFueAgregada(numeroCuenta);
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    boolean cuentaVisible =
        PaginaPortalPagosEmpresas.CUENTA_AGREGADA.of(numeroCuenta).resolveFor(actor).isVisible();
    boolean botonEliminarVisible =
        PaginaPortalPagosEmpresas.BOTON_ELIMINAR_CUENTA.resolveFor(actor).isVisible();

    return cuentaVisible && botonEliminarVisible;
  }
}
