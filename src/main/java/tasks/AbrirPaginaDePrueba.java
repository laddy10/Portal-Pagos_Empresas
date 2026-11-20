package tasks;

import interactions.NavegarA;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import userinterfaces.PaginaDePrueba;

public class AbrirPaginaDePrueba implements Task {

  public static AbrirPaginaDePrueba enElNavegador() {
    return Tasks.instrumented(AbrirPaginaDePrueba.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(NavegarA.laUrl(PaginaDePrueba.URL));
  }
}
