package stepDefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import questions.TituloDeLaPagina;
import tasks.AbrirPaginaDePrueba;

public class InfraestructuraWebStepDefinitions {

  private static final String TAG_RECAPTCHA_FIREFOX =
      "@clic_recaptcha_firefox_soluciones_fijas";

  @Before
  public void prepararEscenario(Scenario scenario) {
    if (scenario.getSourceTagNames().contains(TAG_RECAPTCHA_FIREFOX)) {
      WebDriverManager.firefoxdriver().setup();
      System.setProperty("webdriver.driver", "firefox");
    } else {
      WebDriverManager.chromedriver().setup();
      System.setProperty("webdriver.driver", "chrome");
    }

    OnStage.setTheStage(new OnlineCast());
  }

  @Dado("^que el usuario abre una pagina web de prueba$")
  public void abrirPaginaWebDePrueba() {
    OnStage.theActorCalled("Usuario").attemptsTo(AbrirPaginaDePrueba.enElNavegador());
  }

  @Entonces("^deberia visualizar el titulo \"([^\"]*)\"$")
  public void verificarTitulo(String tituloEsperado) {
    String tituloActual = OnStage.theActorInTheSpotlight().asksFor(TituloDeLaPagina.valor());

    assertThat(tituloActual, is(tituloEsperado));
  }
}
