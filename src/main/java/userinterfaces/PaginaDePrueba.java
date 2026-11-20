package userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class PaginaDePrueba {

  public static final String URL =
      "data:text/html;charset=utf-8,%3Chtml%3E%3Chead%3E%3Ctitle%3ESerenity%20Web%3C/title%3E%3C/head%3E%3Cbody%3E%3Ch1%3EInfraestructura%20WEB%20activa%3C/h1%3E%3C/body%3E%3C/html%3E";

  public static final Target MENSAJE_INFRAESTRUCTURA =
      Target.the("mensaje de infraestructura web").located(By.tagName("h1"));

  private PaginaDePrueba() {}
}
