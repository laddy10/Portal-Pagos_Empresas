package questions;

import java.text.Normalizer;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PagaEnLineaCargo implements Question<Boolean> {

  public static PagaEnLineaCargo correctamente() {
    return new PagaEnLineaCargo();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    String urlActual = driver.getCurrentUrl();
    String textoPagina = normalizar(driver.findElement(By.tagName("body")).getText());

    boolean urlConfirmacion =
        urlActual != null && urlActual.toLowerCase().contains("confirmacion.php");
    boolean contienePagaEnLinea = textoPagina.contains(normalizar("Paga en línea"));

    return urlConfirmacion && contienePagaEnLinea;
  }

  private static String normalizar(String texto) {
    if (texto == null) {
      return "";
    }
    String sinTildes =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    return sinTildes.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
