package interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
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
          "No está configurada la variable PORTAL_PAGOS_PASSWORD. Si la acaba de configurar,"
              + " cierre y vuelva a abrir el IDE (y ejecute 'gradlew --stop'): los programas"
              + " abiertos antes no ven variables nuevas.");
    }

    return Tasks.instrumented(IngresarClaveAccesoPruebas.class, clave);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    WebDriver driver = BrowseTheWeb.as(actor).getDriver();
    WebDriverWait wait = new WebDriverWait(driver, TIEMPO_ESPERA_SEGUNDOS);
    Alert prompt;
    try {
      prompt = wait.until(ExpectedConditions.alertIsPresent());
    } catch (TimeoutException exception) {
      // Con perfil persistente el portal puede no volver a pedir la clave.
      System.out.println("CLAVE_ACCESO_PRUEBAS=NO_SOLICITADA");
      return;
    }

    prompt.sendKeys(clave);
    prompt.accept();
  }
}
