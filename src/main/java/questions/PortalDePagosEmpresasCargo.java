package questions;

import java.net.URI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class PortalDePagosEmpresasCargo implements Question<Boolean> {

  private static final String HOST_PORTAL_PAGOS = "portalpagosempresa.claro.com.co";

  public static PortalDePagosEmpresasCargo correctamente() {
    return new PortalDePagosEmpresasCargo();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    String urlActual = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();

    try {
      return HOST_PORTAL_PAGOS.equalsIgnoreCase(URI.create(urlActual).getHost());
    } catch (IllegalArgumentException exception) {
      return false;
    }
  }
}
