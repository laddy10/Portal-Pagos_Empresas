package stepDefinitions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import cucumber.api.java.es.Cuando;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import interactions.ProbarRecaptchaEdgeConClicNormal;
import interactions.ProbarRecaptchaFirefoxConClicNormal;
import interactions.ResolverRecaptcha;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import questions.BancoPopularPseCargo;
import questions.BotonIngresarBancoHabilitado;
import questions.CuentaFueAgregada;
import questions.FormularioPagoPseCargo;
import questions.MedioDePagoSeleccionado;
import questions.PagaEnLineaCargo;
import questions.PagoSolucionesFijasSeleccionado;
import questions.PortalClaroEmpresasCargo;
import questions.PortalDePagosEmpresasCargo;
import questions.TextosFaltantesEnPagaEnLinea;
import questions.ValorDelCampo;
import tasks.AbrirPortalClaroEmpresas;
import tasks.AccederAPagosYRecargas;
import tasks.AgregarCuenta;
import tasks.CerrarInformacionImportante;
import tasks.ConfirmarFormularioPagoPse;
import tasks.ConfirmarMedioDePago;
import tasks.ContinuarAPagaEnLinea;
import tasks.DiligenciarAccesoBancoPopular;
import tasks.DiligenciarFormularioPagoPse;
import tasks.IngresarNumeroCuenta;
import tasks.SeleccionarMedioDePago;
import tasks.SeleccionarPagoSolucionesFijas;
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
        "El host final debería ser portalpagosempresa.claro.com.co", portalDePagosCargo, is(true));
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
    OnStage.theActorInTheSpotlight().attemptsTo(ProbarRecaptchaEdgeConClicNormal.enElCheckbox());
  }

  @Entonces("^el reCAPTCHA debería quedar aprobado en Edge$")
  public void verificarRecaptchaAprobadoEnEdge() {
    Boolean recaptchaAprobado =
        OnStage.theActorInTheSpotlight()
            .recall(ProbarRecaptchaEdgeConClicNormal.RECAPTCHA_EDGE_APROBADO);

    assertThat("El reCAPTCHA debería tener aria-checked=true en Edge", recaptchaAprobado, is(true));
  }

  @Cuando("^hace clic normal en el reCAPTCHA usando Firefox$")
  public void hacerClicNormalEnElRecaptchaUsandoFirefox() {
    OnStage.theActorInTheSpotlight().attemptsTo(ProbarRecaptchaFirefoxConClicNormal.enElCheckbox());
  }

  @Entonces("^el reCAPTCHA debería quedar aprobado en Firefox$")
  public void verificarRecaptchaAprobadoEnFirefox() {
    Boolean recaptchaAprobado =
        OnStage.theActorInTheSpotlight()
            .recall(ProbarRecaptchaFirefoxConClicNormal.RECAPTCHA_FIREFOX_APROBADO);

    assertThat(
        "El reCAPTCHA debería tener aria-checked=true en Firefox", recaptchaAprobado, is(true));
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

  @Cuando("^resuelve el reCAPTCHA usando la sesión persistente$")
  public void resolverRecaptchaConSesionPersistente() {
    OnStage.theActorInTheSpotlight().attemptsTo(ResolverRecaptcha.enElCheckbox());
  }

  @Entonces("^el reCAPTCHA debería quedar aprobado$")
  public void verificarRecaptchaAprobado() {
    Boolean aprobado =
        OnStage.theActorInTheSpotlight().recall(ResolverRecaptcha.RECAPTCHA_APROBADO);
    Boolean requirioDesafio =
        OnStage.theActorInTheSpotlight().recall(ResolverRecaptcha.RECAPTCHA_REQUIRIO_DESAFIO);

    System.out.println("RECAPTCHA_REQUIRIO_DESAFIO=" + requirioDesafio);
    assertThat("El reCAPTCHA debería tener aria-checked=true", aprobado, is(true));
  }

  @Cuando("^agrega la cuenta \"([^\"]*)\"$")
  public void agregarLaCuenta(String numeroCuenta) {
    OnStage.theActorInTheSpotlight().attemptsTo(AgregarCuenta.conNumero(numeroCuenta));
  }

  @Entonces("^la cuenta \"([^\"]*)\" debería quedar agregada$")
  public void verificarCuentaAgregada(String numeroCuenta) {
    boolean cuentaAgregada =
        OnStage.theActorInTheSpotlight().asksFor(CuentaFueAgregada.conNumero(numeroCuenta));

    assertThat(
        "La cuenta " + numeroCuenta + " debería mostrarse agregada con la opción Eliminar",
        cuentaAgregada,
        is(true));
  }

  @Cuando("^hace clic en Continuar hacia Paga en línea$")
  public void hacerClicEnContinuarHaciaPagaEnLinea() {
    OnStage.theActorInTheSpotlight().attemptsTo(ContinuarAPagaEnLinea.ahora());
  }

  @Entonces("^debería ingresar a la pantalla Paga en línea$")
  public void verificarIngresoAPagaEnLinea() {
    boolean pagaEnLineaCargo =
        OnStage.theActorInTheSpotlight().asksFor(PagaEnLineaCargo.correctamente());

    assertThat(
        "Debería haber ingresado a la pantalla Paga en línea (confirmacion.php)",
        pagaEnLineaCargo,
        is(true));
  }

  @Entonces("^deberían mostrarse los textos de detalle del pago:$")
  public void verificarTextosDeDetalleDelPago(List<String> textosEsperados) {
    List<String> faltantes =
        OnStage.theActorInTheSpotlight()
            .asksFor(TextosFaltantesEnPagaEnLinea.para(textosEsperados));

    assertThat(
        "Faltan textos esperados en la pantalla Paga en línea: " + faltantes,
        faltantes,
        is(Collections.emptyList()));
  }

  @Entonces("^debería mostrarse la referencia de pago \"([^\"]*)\" en el detalle$")
  public void verificarReferenciaDePagoEnElDetalle(String referenciaEsperada) {
    boolean referenciaVisible =
        PaginaPortalPagosEmpresas.REFERENCIA_DE_PAGO_EN_DETALLE
            .of(referenciaEsperada)
            .resolveFor(OnStage.theActorInTheSpotlight())
            .isVisible();

    assertThat(
        "La referencia de pago " + referenciaEsperada + " debería mostrarse en Detalle de tu pago",
        referenciaVisible,
        is(true));
  }

  @Cuando("^despliega Elige una opción y selecciona el medio de pago \"([^\"]*)\"$")
  public void seleccionarMedioDePago(String medioDePago) {
    OnStage.theActorInTheSpotlight().attemptsTo(SeleccionarMedioDePago.conNombre(medioDePago));
  }

  @Entonces("^el medio de pago \"([^\"]*)\" debería quedar seleccionado$")
  public void verificarMedioDePagoSeleccionado(String medioDePago) {
    boolean seleccionado =
        OnStage.theActorInTheSpotlight().asksFor(MedioDePagoSeleccionado.es(medioDePago));

    assertThat(
        "El medio de pago " + medioDePago + " debería quedar seleccionado", seleccionado, is(true));
  }

  @Cuando("^hace clic en el botón Confirmar$")
  public void hacerClicEnElBotonConfirmar() {
    OnStage.theActorInTheSpotlight().attemptsTo(ConfirmarMedioDePago.ahora());
  }

  @Entonces("^debería ser direccionado al formulario de pago PSE$")
  public void verificarDireccionamientoAlFormularioDePagoPse() {
    boolean formularioCargo =
        OnStage.theActorInTheSpotlight().asksFor(FormularioPagoPseCargo.correctamente());

    assertThat(
        "Debería haberse direccionado al formulario de pago PSE (pagos.php)",
        formularioCargo,
        is(true));
  }

  @Entonces("^deberían mostrarse los textos del resumen de pago:$")
  public void verificarTextosDelResumenDePago(List<String> textosEsperados) {
    List<String> faltantes =
        OnStage.theActorInTheSpotlight()
            .asksFor(TextosFaltantesEnPagaEnLinea.para(textosEsperados));

    assertThat(
        "Faltan textos esperados en el resumen del formulario de pago: " + faltantes,
        faltantes,
        is(Collections.emptyList()));
  }

  @Cuando("^diligencia el formulario de pago PSE con los datos:$")
  public void diligenciarFormularioDePagoPse(Map<String, String> datosFormulario) {
    OnStage.theActorInTheSpotlight()
        .attemptsTo(DiligenciarFormularioPagoPse.conDatos(datosFormulario));
  }

  @Cuando("^hace clic en Confirmar en el formulario de pago PSE$")
  public void confirmarFormularioDePagoPse() {
    OnStage.theActorInTheSpotlight().attemptsTo(ConfirmarFormularioPagoPse.ahora());
  }

  @Entonces("^debería ser direccionado al formulario del banco Popular$")
  public void verificarDireccionamientoAlBancoPopular() {
    boolean bancoCargo =
        OnStage.theActorInTheSpotlight().asksFor(BancoPopularPseCargo.correctamente());

    assertThat(
        "Debería haberse direccionado al formulario de Banco Popular PSE", bancoCargo, is(true));
  }

  @Cuando("^ingresa el documento del formulario y la contraseña \"([^\"]*)\" en el banco$")
  public void ingresarCredencialesEnElBanco(String contrasena) {
    OnStage.theActorInTheSpotlight()
        .attemptsTo(DiligenciarAccesoBancoPopular.conContrasena(contrasena));
  }

  @Entonces("^el botón Ingresar del banco debería quedar habilitado$")
  public void verificarBotonIngresarHabilitado() {
    boolean botonHabilitado =
        OnStage.theActorInTheSpotlight().asksFor(BotonIngresarBancoHabilitado.correctamente());

    assertThat(
        "El botón Ingresar del banco Popular debería estar habilitado", botonHabilitado, is(true));
  }
}
