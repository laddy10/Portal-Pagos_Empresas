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
import userinterfaces.PaginaBancoPopularPse;
import userinterfaces.PaginaFormularioPagoPse;

public class ConfirmarFormularioPagoPse implements Task {

  private static final int TIEMPO_ESPERA_BOTON_SEGUNDOS = 15;
  private static final int TIEMPO_ESPERA_BANCO_SEGUNDOS = 45;

  public static ConfirmarFormularioPagoPse ahora() {
    return Tasks.instrumented(ConfirmarFormularioPagoPse.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        WaitUntil.the(PaginaFormularioPagoPse.BOTON_CONFIRMAR, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_BOTON_SEGUNDOS)
            .seconds(),
        Scroll.to(PaginaFormularioPagoPse.BOTON_CONFIRMAR),
        WaitUntil.the(PaginaFormularioPagoPse.BOTON_CONFIRMAR, isClickable())
            .forNoMoreThan(TIEMPO_ESPERA_BOTON_SEGUNDOS)
            .seconds(),
        Click.on(PaginaFormularioPagoPse.BOTON_CONFIRMAR),
        WaitUntil.the(PaginaBancoPopularPse.CAMPO_NUMERO_DOCUMENTO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_BANCO_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Direccionamiento al portal Banco Popular PSE"));
  }
}
