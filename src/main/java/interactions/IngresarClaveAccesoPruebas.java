package interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IngresarClaveAccesoPruebas implements Interaction {

  private static final int TIEMPO_ESPERA_SEGUNDOS = 10;

  private final String clave;

  public IngresarClaveAccesoPruebas(String clave) {
    this.clave = clave;
  }

  public static IngresarClaveAccesoPruebas con(String clave) {
    if (clave == null || clave.trim().isEmpty()) {
      throw new IllegalStateException(
          "No está configurada la variable PORTAL_PAGOS_PASSWORD");
    }

    return Tasks.instrumented(IngresarClaveAccesoPruebas.class, clave);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    WebDriverWait wait = new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS);
    Alert prompt = wait.until(ExpectedConditions.alertIsPresent());

    prompt.sendKeys(clave);
    prompt.accept();
  }
}
