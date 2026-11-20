package interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class NavegarA implements Interaction {

  private final String url;

  public NavegarA(String url) {
    this.url = url;
  }

  public static NavegarA laUrl(String url) {
    return Tasks.instrumented(NavegarA.class, url);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    BrowseTheWeb.as(actor).getDriver().get(url);
  }
}
