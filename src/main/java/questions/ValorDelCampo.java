package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.targets.Target;

public class ValorDelCampo implements Question<String> {

  private final Target campo;

  public ValorDelCampo(Target campo) {
    this.campo = campo;
  }

  public static ValorDelCampo de(Target campo) {
    return new ValorDelCampo(campo);
  }

  @Override
  public String answeredBy(Actor actor) {
    return campo.resolveFor(actor).getValue();
  }
}
