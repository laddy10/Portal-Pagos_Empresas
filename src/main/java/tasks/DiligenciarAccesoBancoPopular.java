package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isEnabled;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import userinterfaces.PaginaBancoPopularPse;

public class DiligenciarAccesoBancoPopular implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 15;

  private final String contrasena;

  public DiligenciarAccesoBancoPopular(String contrasena) {
    this.contrasena = contrasena;
  }

  public static DiligenciarAccesoBancoPopular conContrasena(String contrasena) {
    return Tasks.instrumented(DiligenciarAccesoBancoPopular.class, contrasena);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    String numeroDocumento = actor.recall(DiligenciarFormularioPagoPse.NUMERO_DOCUMENTO_INGRESADO);
    if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
      throw new IllegalStateException(
          "No se encontró el número de documento ingresado previamente en el formulario PSE.");
    }
    String documentoSoloNumeros = numeroDocumento.replaceAll("[^0-9]", "");

    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    JavascriptExecutor js = (JavascriptExecutor) driver;

    actor.attemptsTo(
        WaitUntil.the(PaginaBancoPopularPse.CAMPO_NUMERO_DOCUMENTO, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds());

    WebElement campoDoc = PaginaBancoPopularPse.CAMPO_NUMERO_DOCUMENTO.resolveFor(actor);
    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", campoDoc);

    actor.attemptsTo(
        Enter.theValue(documentoSoloNumeros).into(PaginaBancoPopularPse.CAMPO_NUMERO_DOCUMENTO),
        WaitUntil.the(PaginaBancoPopularPse.CAMPO_CONTRASENA, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds());

    WebElement campoPass = PaginaBancoPopularPse.CAMPO_CONTRASENA.resolveFor(actor);
    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", campoPass);

    actor.attemptsTo(
        Enter.theValue(contrasena).into(PaginaBancoPopularPse.CAMPO_CONTRASENA),
        WaitUntil.the(PaginaBancoPopularPse.BOTON_INGRESAR, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        WaitUntil.the(PaginaBancoPopularPse.BOTON_INGRESAR, isEnabled())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds());

    WebElement botonIngresar = PaginaBancoPopularPse.BOTON_INGRESAR.resolveFor(actor);
    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", botonIngresar);

    actor.attemptsTo(
        CapturarEvidencia.titulada(
            "Datos ingresados en Banco Popular PSE y botón Ingresar habilitado"));
  }
}
