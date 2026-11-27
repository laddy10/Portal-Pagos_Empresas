package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import userinterfaces.PaginaBancoPopularPse;

public class BancoPopularPseCargo implements Question<Boolean> {

  public static BancoPopularPseCargo correctamente() {
    return new BancoPopularPseCargo();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    String urlActual = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
    boolean urlBanco = urlActual != null && urlActual.toLowerCase().contains("bancopopular.com.co");
    boolean campoDocumentoVisible =
        PaginaBancoPopularPse.CAMPO_NUMERO_DOCUMENTO.resolveFor(actor).isVisible();
    boolean campoContrasenaVisible =
        PaginaBancoPopularPse.CAMPO_CONTRASENA.resolveFor(actor).isVisible();

    return urlBanco && campoDocumentoVisible && campoContrasenaVisible;
  }
}
