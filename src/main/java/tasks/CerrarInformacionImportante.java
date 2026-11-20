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

public class CerrarInformacionImportante implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 10;

  public static CerrarInformacionImportante conEntendido() {
    return Tasks.instrumented(CerrarInformacionImportante.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.MODAL_INFORMACION_IMPORTANTE, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        WaitUntil.the(PaginaPortalPagosEmpresas.TITULO_MODAL_INFORMACION_IMPORTANTE, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Visualización del modal “Información Importante”"),
        Click.on(PaginaPortalPagosEmpresas.BOTON_ENTENDIDO),
        WaitUntil.the(PaginaPortalPagosEmpresas.MODAL_INFORMACION_IMPORTANTE, isNotVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds());
  }
}
