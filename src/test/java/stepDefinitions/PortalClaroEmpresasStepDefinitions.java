package stepDefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import cucumber.api.java.es.Cuando;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import interactions.ProbarRecaptchaEdgeConClicNormal;
import interactions.ProbarRecaptchaFirefoxConClicNormal;
import java.net.URI;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import questions.PagoSolucionesFijasSeleccionado;
import questions.PortalClaroEmpresasCargo;
import questions.PortalDePagosEmpresasCargo;
import questions.ValorDelCampo;
import tasks.AbrirPortalClaroEmpresas;
import tasks.AccederAPagosYRecargas;
import tasks.CerrarInformacionImportante;
import tasks.IngresarNumeroCuenta;
import tasks.SeleccionarPagoSolucionesFijas;
import userinterfaces.PaginaClaroEmpresas;
import userinterfaces.PaginaPortalPagosEmpresas;

public class PortalClaroEmpresasStepDefinitions {

  @Dado("^que el usuario abre el portal Claro Empresas$")
  public void abrirPortalClaroEmpresas() {
    OnStage.theActorCalled("Usuario").attemptsTo(AbrirPortalClaroEmpresas.enElNavegador());
  }

  @Dado("^que el usuario abre el portal Claro Empresas usando Edge$")
  public void abrirPortalClaroEmpresasUsandoEdge() {
    OnStage.theActorCalled("Usuario").attemptsTo(AbrirPortalClaroEmpresas.enElNavegador());
  }

  @Dado("^que el usuario abre el portal Claro Empresas usando Firefox$")
  public void abrirPortalClaroEmpresasUsandoFirefox() {
    OnStage.theActorCalled("Usuario").attemptsTo(AbrirPortalClaroEmpresas.enElNavegador());
  }

  @Entonces("^el portal Claro Empresas debería cargar correctamente$")
  public void verificarCargaDelPortalClaroEmpresas() {
    String urlFinal = BrowseTheWeb.as(OnStage.theActorInTheSpotlight()).getDriver().getCurrentUrl();
    boolean portalVisible =
        OnStage.theActorInTheSpotlight().asksFor(PortalClaroEmpresasCargo.correctamente());

    URI uriFinal = URI.create(urlFinal);
    String origenFinal = uriFinal.getScheme() + "://" + uriFinal.getHost();

    assertThat(
        "El origen final debería ser el Portal de Pagos de pruebas",
        origenFinal,
        is("https://empresaspre.portalpagospre.claro.com.co"));
    assertThat("El elemento main#cPal debería estar visible", portalVisible, is(true));
  }

  @Cuando("^accede a Pagos y Recargas$")
  public void accederAPagosYRecargas() {
    OnStage.theActorInTheSpotlight().attemptsTo(AccederAPagosYRecargas.desdeElPortal());
  }

  @Entonces("^debería ser dirigido al Portal de Pagos Empresas$")
  public void verificarRedireccionAlPortalDePagosEmpresas() {
    boolean portalDePagosCargo =
        OnStage.theActorInTheSpotlight().asksFor(PortalDePagosEmpresasCargo.correctamente());

    assertThat(
        "El host final debería ser portalpagosempresa.claro.com.co",
        portalDePagosCargo,
        is(true));
  }

  @Cuando("^cierra el modal Información Importante$")
  public void cerrarModalInformacionImportante() {
    OnStage.theActorInTheSpotlight().attemptsTo(CerrarInformacionImportante.conEntendido());
  }

  @Cuando("^selecciona Pago Soluciones Fijas$")
  public void seleccionarPagoSolucionesFijas() {
    OnStage.theActorInTheSpotlight().attemptsTo(SeleccionarPagoSolucionesFijas.fijas());
  }

  @Entonces("^la opción Pago Soluciones Fijas debería quedar seleccionada$")
  public void verificarPagoSolucionesFijasSeleccionado() {
    boolean opcionSeleccionada =
        OnStage.theActorInTheSpotlight().asksFor(PagoSolucionesFijasSeleccionado.correctamente());

    assertThat(
        "Pago Soluciones Fijas debería estar seleccionado y las opciones ocultas",
        opcionSeleccionada,
        is(true));
  }

  @Cuando("^ingresa el número de cuenta \"([^\"]*)\"$")
  public void ingresaElNumeroDeCuenta(String numeroCuenta) {
    OnStage.theActorInTheSpotlight().attemptsTo(IngresarNumeroCuenta.con(numeroCuenta));
  }

  @Cuando("^hace clic normal en el reCAPTCHA usando Edge$")
  public void hacerClicNormalEnElRecaptchaUsandoEdge() {
    OnStage.theActorInTheSpotlight()
        .attemptsTo(ProbarRecaptchaEdgeConClicNormal.enElCheckbox());
  }

  @Entonces("^el reCAPTCHA debería quedar aprobado en Edge$")
  public void verificarRecaptchaAprobadoEnEdge() {
    Boolean recaptchaAprobado =
        OnStage.theActorInTheSpotlight()
            .recall(ProbarRecaptchaEdgeConClicNormal.RECAPTCHA_EDGE_APROBADO);

    assertThat(
        "El reCAPTCHA debería tener aria-checked=true en Edge",
        recaptchaAprobado,
        is(true));
  }

  @Cuando("^hace clic normal en el reCAPTCHA usando Firefox$")
  public void hacerClicNormalEnElRecaptchaUsandoFirefox() {
    OnStage.theActorInTheSpotlight()
        .attemptsTo(ProbarRecaptchaFirefoxConClicNormal.enElCheckbox());
  }

  @Entonces("^el reCAPTCHA debería quedar aprobado en Firefox$")
  public void verificarRecaptchaAprobadoEnFirefox() {
    Boolean recaptchaAprobado =
        OnStage.theActorInTheSpotlight()
            .recall(ProbarRecaptchaFirefoxConClicNormal.RECAPTCHA_FIREFOX_APROBADO);

    assertThat(
        "El reCAPTCHA debería tener aria-checked=true en Firefox",
        recaptchaAprobado,
        is(true));
  }

  @Entonces("^el campo número de cuenta debería contener \"([^\"]*)\"$")
  public void verificarNumeroDeCuenta(String numeroCuentaEsperado) {
    String numeroCuentaActual =
        OnStage.theActorInTheSpotlight()
            .asksFor(ValorDelCampo.de(PaginaPortalPagosEmpresas.CAMPO_NUMERO_CUENTA));

    assertThat(
        "El campo número de cuenta debería contener el valor ingresado",
        numeroCuentaActual,
        is(numeroCuentaEsperado));
  }
}
