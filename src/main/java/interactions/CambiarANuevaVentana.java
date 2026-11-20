package interactions;

import java.util.HashSet;
import java.util.Set;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CambiarANuevaVentana implements Interaction {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 10;

  private final Set<String> ventanasAnteriores;

  public CambiarANuevaVentana(Set<String> ventanasAnteriores) {
    this.ventanasAnteriores = new HashSet<>(ventanasAnteriores);
  }

  public static CambiarANuevaVentana queNoEstabaEn(Set<String> ventanasAnteriores) {
    return Tasks.instrumented(CambiarANuevaVentana.class, ventanasAnteriores);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    WebDriverWait wait = new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS);

    String nuevaVentana =
        wait.until(
            navegador ->
                navegador.getWindowHandles().stream()
                    .filter(ventana -> !ventanasAnteriores.contains(ventana))
                    .findFirst()
                    .orElse(null));

    driver.switchTo().window(nuevaVentana);
  }
}
