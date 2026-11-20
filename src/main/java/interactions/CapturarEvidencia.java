package interactions;

import evidencias.CapturaEscritorio;
import evidencias.ContextoEvidencias;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;

public class CapturarEvidencia implements Interaction {

  private final String titulo;

  public CapturarEvidencia(String titulo) {
    this.titulo = titulo;
  }

  public static CapturarEvidencia titulada(String titulo) {
    return Tasks.instrumented(CapturarEvidencia.class, titulo);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    if (ContextoEvidencias.estaActivo()) {
      WebDriver driver = BrowseTheWeb.as(actor).getDriver();
      CapturaEscritorio.capturar(titulo, driver.getTitle());
    }
  }
}
