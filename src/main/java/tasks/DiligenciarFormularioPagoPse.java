package tasks;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

import interactions.CapturarEvidencia;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import userinterfaces.PaginaFormularioPagoPse;

public class DiligenciarFormularioPagoPse implements Task {

  public static final String NUMERO_DOCUMENTO_INGRESADO = "numeroDocumentoPseIngresado";
  private static final int TIEMPO_ESPERA_SEGUNDOS = 15;

  private final String banco;
  private final String titular;
  private final String tipoCliente;
  private final String tipoDocumento;
  private final String numeroDocumento;
  private final String numeroTelefono;
  private final String correoElectronico;

  public DiligenciarFormularioPagoPse(
      String banco,
      String titular,
      String tipoCliente,
      String tipoDocumento,
      String numeroDocumento,
      String numeroTelefono,
      String correoElectronico) {
    this.banco = banco;
    this.titular = titular;
    this.tipoCliente = tipoCliente;
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
    this.numeroTelefono = numeroTelefono;
    this.correoElectronico = correoElectronico;
  }

  public static DiligenciarFormularioPagoPse conDatos(Map<String, String> datos) {
    return Tasks.instrumented(
        DiligenciarFormularioPagoPse.class,
        datos.get("banco"),
        datos.get("titular"),
        datos.get("tipoCliente"),
        datos.get("tipoDocumento"),
        datos.get("numeroDocumento"),
        datos.get("numeroTelefono"),
        datos.get("correoElectronico"));
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    seleccionarOpcion(actor, PaginaFormularioPagoPse.SELECT_BANCO, banco);
    diligenciarCampo(actor, PaginaFormularioPagoPse.CAMPO_NOMBRE_TITULAR, titular);
    seleccionarOpcion(actor, PaginaFormularioPagoPse.SELECT_TIPO_CLIENTE, tipoCliente);
    seleccionarOpcion(actor, PaginaFormularioPagoPse.SELECT_TIPO_DOCUMENTO, tipoDocumento);
    diligenciarCampo(actor, PaginaFormularioPagoPse.CAMPO_NUMERO_DOCUMENTO, numeroDocumento);
    actor.remember(NUMERO_DOCUMENTO_INGRESADO, numeroDocumento);
    diligenciarCampo(actor, PaginaFormularioPagoPse.CAMPO_NUMERO_TELEFONO, numeroTelefono);
    diligenciarCampo(actor, PaginaFormularioPagoPse.CAMPO_CORREO_ELECTRONICO, correoElectronico);

    actor.attemptsTo(
        Scroll.to(PaginaFormularioPagoPse.SELECT_BANCO),
        CapturarEvidencia.titulada("Formulario de pago PSE diligenciado (parte superior)"),
        Scroll.to(PaginaFormularioPagoPse.CAMPO_CORREO_ELECTRONICO),
        CapturarEvidencia.titulada("Formulario de pago PSE diligenciado (parte inferior)"));
  }

  private static void diligenciarCampo(Actor actor, Target campo, String valor) {
    actor.attemptsTo(
        WaitUntil.the(campo, isVisible()).forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS).seconds(),
        Scroll.to(campo),
        Enter.theValue(valor).into(campo));
  }

  private static void seleccionarOpcion(Actor actor, Target selectorTarget, String valorEsperado) {
    actor.attemptsTo(
        WaitUntil.the(selectorTarget, isVisible()).forNoMoreThan(TIEMPO_ESPERA_SEGUNDOS).seconds(),
        Scroll.to(selectorTarget));

    Select select = new Select(selectorTarget.resolveFor(actor));
    String objetivoNorm = normalizar(valorEsperado);

    // 1. Coincidencia exacta (normalizada)
    for (WebElement opcion : select.getOptions()) {
      if (normalizar(opcion.getText()).equals(objetivoNorm)) {
        select.selectByVisibleText(opcion.getText());
        return;
      }
    }

    // 2. Coincidencia parcial (ej. "Popular" -> "BANCO POPULAR")
    List<String> disponibles = new ArrayList<>();
    for (WebElement opcion : select.getOptions()) {
      String texto = opcion.getText();
      String textoNorm = normalizar(texto);
      if (!textoNorm.isEmpty()) {
        disponibles.add(texto.trim());
        if (textoNorm.contains(objetivoNorm) || objetivoNorm.contains(textoNorm)) {
          select.selectByVisibleText(texto);
          return;
        }
      }
    }

    throw new IllegalStateException(
        "No se encontró la opción '"
            + valorEsperado
            + "' en "
            + selectorTarget.getName()
            + ". Opciones disponibles: "
            + disponibles);
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
