package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CambiarANuevaVentana;
import interactions.CapturarEvidencia;
import java.util.HashSet;
import java.util.Set;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.waits.WaitUntil;
import userinterfaces.PaginaClaroEmpresas;
import userinterfaces.PaginaPortalPagosEmpresas;

public class AccederAPagosYRecargas implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 20;

  public static AccederAPagosYRecargas desdeElPortal() {
    return Tasks.instrumented(AccederAPagosYRecargas.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    Set<String> ventanasAnteriores =
        new HashSet<>(BrowseTheWeb.as(actor).getDriver().getWindowHandles());

    actor.attemptsTo(
        Scroll.to(PaginaClaroEmpresas.SECCION_PAGOS_Y_RECARGAS),
        WaitUntil.the(PaginaClaroEmpresas.PAGUE_Y_RECARGUE_AQUI, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Acceso a Pagos y Recargas"),
        Click.on(PaginaClaroEmpresas.PAGUE_Y_RECARGUE_AQUI),
        CambiarANuevaVentana.queNoEstabaEn(ventanasAnteriores),
        WaitUntil.the(PaginaPortalPagosEmpresas.SELECTOR_DE_SERVICIO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Ingreso al Portal de Pagos Empresas"));
  }
}
