package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class TituloDeLaPagina implements Question<String> {

  public static TituloDeLaPagina valor() {
    return new TituloDeLaPagina();
  }

  @Override
  public String answeredBy(Actor actor) {
    return BrowseTheWeb.as(actor).getTitle();
  }
}
