package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import interactions.IngresarClaveAccesoPruebas;
import interactions.NavegarA;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.waits.WaitUntil;
import userinterfaces.PaginaClaroEmpresas;
import userinterfaces.PaginaPortalPagosEmpresas;

public class AbrirPortalClaroEmpresas implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 20;

  public static AbrirPortalClaroEmpresas enElNavegador() {
    return Tasks.instrumented(AbrirPortalClaroEmpresas.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    String clave = System.getenv("PORTAL_PAGOS_PASSWORD");

    actor.attemptsTo(
        NavegarA.laUrl(PaginaClaroEmpresas.URL),
        IngresarClaveAccesoPruebas.con(clave),
        WaitUntil.the(PaginaPortalPagosEmpresas.SELECTOR_DE_SERVICIO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Ingreso al portal Claro Empresas"));
  }
}
