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
import userinterfaces.PaginaFormularioPagoPse;
import userinterfaces.PaginaPortalPagosEmpresas;

public class ConfirmarMedioDePago implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 25;

  public static ConfirmarMedioDePago ahora() {
    return Tasks.instrumented(ConfirmarMedioDePago.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_CONFIRMAR_MEDIO_PAGO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Scroll.to(PaginaPortalPagosEmpresas.BOTON_CONFIRMAR_MEDIO_PAGO),
        WaitUntil.the(PaginaPortalPagosEmpresas.BOTON_CONFIRMAR_MEDIO_PAGO, isClickable())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Click.on(PaginaPortalPagosEmpresas.BOTON_CONFIRMAR_MEDIO_PAGO),
        WaitUntil.the(PaginaFormularioPagoPse.SELECT_BANCO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Ingreso al formulario de pago PSE"));
  }
}
