package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.waits.WaitUntil;
import userinterfaces.PaginaPortalPagosEmpresas;

public class AgregarCuenta implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 15;

  private final String numeroCuenta;

  public AgregarCuenta(String numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

  public static AgregarCuenta conNumero(String numeroCuenta) {
    return Tasks.instrumented(AgregarCuenta.class, numeroCuenta);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_AGREGAR, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Scroll.to(PaginaPortalPagosEmpresas.BOTON_AGREGAR),
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_AGREGAR, isClickable())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Click.on(PaginaPortalPagosEmpresas.BOTON_AGREGAR),
        WaitUntil.the(PaginaPortalPagosEmpresas.CUENTA_AGREGADA.of(numeroCuenta), isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_ELIMINAR_CUENTA, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Cuenta agregada " + numeroCuenta));
  }
}
