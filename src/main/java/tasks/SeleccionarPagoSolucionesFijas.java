package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import userinterfaces.PaginaPortalPagosEmpresas;

public class SeleccionarPagoSolucionesFijas implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 10;

  public static SeleccionarPagoSolucionesFijas fijas() {
    return Tasks.instrumented(SeleccionarPagoSolucionesFijas.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        Click.on(PaginaPortalPagosEmpresas.SELECTOR_DE_SERVICIO),
        WaitUntil.the(PaginaPortalPagosEmpresas.CONTENEDOR_DE_OPCIONES, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Apertura del selector de servicios"),
        Click.on(PaginaPortalPagosEmpresas.OPCION_PAGO_SOLUCIONES_FIJAS),
        WaitUntil.the(PaginaPortalPagosEmpresas.PAGO_SOLUCIONES_FIJAS_SELECCIONADO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        WaitUntil.the(PaginaPortalPagosEmpresas.CONTENEDOR_DE_OPCIONES, isNotVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Selección del servicio “Pago Soluciones Fijas”"));
  }
}
