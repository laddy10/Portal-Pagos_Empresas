package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import userinterfaces.PaginaPortalPagosEmpresas;

public class IngresarNumeroCuenta implements Task {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 10;

  private final String numeroCuenta;

  public IngresarNumeroCuenta(String numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

  public static IngresarNumeroCuenta con(String numeroCuenta) {
    return Tasks.instrumented(IngresarNumeroCuenta.class, numeroCuenta);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        WaitUntil.the(PaginaPortalPagosEmpresas.CAMPO_NUMERO_CUENTA, isVisible())
            .forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS)
            .seconds(),
        Enter.theValue(numeroCuenta).into(PaginaPortalPagosEmpresas.CAMPO_NUMERO_CUENTA),
        CapturarEvidencia.titulada("Ingreso del número de cuenta"));
  }
}
