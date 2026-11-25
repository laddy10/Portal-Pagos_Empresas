package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import userinterfaces.PaginaPortalPagosEmpresas;

public class CerrarInformacionImportante implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 10;
  private static final int SEGUNDOS_DETECCION_MODAL = 6;

  public static CerrarInformacionImportante conEntendido() {
    return Tasks.instrumented(CerrarInformacionImportante.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    if (!modalSeMuestra(actor)) {
      // Con perfil persistente la cookie del modal ya existe y el portal no lo vuelve a mostrar.
      System.out.println("MODAL_INFORMACION_IMPORTANTE=NO_MOSTRADO");
      return;
    }

    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.TITULO_MODAL_INFORMACION_IMPORTANTE, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        CapturarEvidencia.titulada("Visualización del modal “Información Importante”"),
        Click.on(PaginaPortalPagosEmpresas.BOTON_ENTENDIDO),
        WaitUntil.the(PaginaPortalPagosEmpresas.MODAL_INFORMACION_IMPORTANTE, isNotVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds());
  }

  private static boolean modalSeMuestra(Actor actor) {
    try {
      new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), SEGUNDOS_DETECCION_MODAL)
          .until(
              ExpectedConditions.visibilityOfElementLocated(
                  By.cssSelector(".modal-dialog.modal-info")));
      return true;
    } catch (TimeoutException exception) {
      return false;
    }
  }
}
