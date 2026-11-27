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

public class ContinuarAPagaEnLinea implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 20;

  public static ContinuarAPagaEnLinea ahora() {
    return Tasks.instrumented(ContinuarAPagaEnLinea.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_CONTINUAR, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Scroll.to(PaginaPortalPagosEmpresas.BOTON_CONTINUAR),
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_CONTINUAR, isClickable())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Click.on(PaginaPortalPagosEmpresas.BOTON_CONTINUAR),
        WaitUntil.the(PaginaPortalPagosEmpresas.ENCABEZADO_VALOR_A_PAGAR, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Ingreso a Paga en línea"));

    if (PaginaPortalPagosEmpresas.TITULO_DETALLE_DE_TU_PAGO.resolveFor(actor).isPresent()) {
      actor.attemptsTo(
          Scroll.to(PaginaPortalPagosEmpresas.TITULO_DETALLE_DE_TU_PAGO),
          CapturarEvidencia.titulada("Detalle de tu pago y Referencia de pago"));
    }
  }
}
