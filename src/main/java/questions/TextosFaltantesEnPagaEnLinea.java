package questions;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TextosFaltantesEnPagaEnLinea implements Question<List<String>> {

  private final List<String> textosEsperados;

  public TextosFaltantesEnPagaEnLinea(List<String> textosEsperados) {
    this.textosEsperados = textosEsperados;
  }

  public static TextosFaltantesEnPagaEnLinea para(List<String> textosEsperados) {
    return new TextosFaltantesEnPagaEnLinea(textosEsperados);
  }

  @Override
  public List<String> answeredBy(Actor actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    String textoPagina = normalizar(driver.findElement(By.tagName("body")).getText());

    List<String> faltantes = new ArrayList<>();
    for (String textoEsperado : textosEsperados) {
      if (!textoPagina.contains(normalizar(textoEsperado))) {
        faltantes.add(textoEsperado);
      }
    }
    return faltantes;
  }

  private static String normalizar(String texto) {
    if (texto == null) {
      return "";
    }
    String sinTildes =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    // Unifica guiones (hyphen, en-dash, em-dash, minus) y espacios múltiples
    String guionesUnificados = sinTildes.replaceAll("[\\u2010-\\u2015\\u2212]", "-");
    return guionesUnificados.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
