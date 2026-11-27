package userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class PaginaPortalPagosEmpresas {

  public static final Target MODAL_INFORMACION_IMPORTANTE =
      Target.the("modal Información Importante")
          .located(By.cssSelector(".modal-dialog.modal-info"));

  public static final Target TITULO_MODAL_INFORMACION_IMPORTANTE =
      Target.the("título del modal Información Importante")
          .located(By.cssSelector("h3.modal-cookie_title"));

  public static final Target BOTON_ENTENDIDO =
      Target.the("botón Entendido")
          .located(By.cssSelector("input.class-boton-conf[value='Entendido']"));

  public static final Target SELECTOR_DE_SERVICIO =
      Target.the("selector de servicio").located(By.cssSelector("div#select.select"));

  public static final Target CONTENEDOR_DE_OPCIONES =
      Target.the("contenedor de opciones de servicio")
          .located(By.cssSelector("div#opciones.opciones"));

  public static final Target OPCION_PAGO_SOLUCIONES_FIJAS =
      Target.the("opción Pago Soluciones Fijas")
          .located(By.cssSelector("#opciones a.opcion[data-target='#10001']"));

  public static final Target PAGO_SOLUCIONES_FIJAS_SELECCIONADO =
      Target.the("Pago Soluciones Fijas seleccionado")
          .located(By.cssSelector("#select [data-codigo='10001']"));

  public static final Target CAMPO_NUMERO_CUENTA =
      Target.the("campo número de cuenta").located(By.cssSelector("#newReference"));

  public static final Target BOTON_AGREGAR =
      Target.the("botón Agregar cuenta").located(By.id("agregar"));

  public static final Target CUENTA_AGREGADA =
      Target.the("cuenta agregada {0}")
          .locatedBy("#contentReferencias #refRow{0} input#ref{0}[value='{0}']");

  public static final Target BOTON_ELIMINAR_CUENTA =
      Target.the("botón Eliminar cuenta")
          .located(By.cssSelector("#contentReferencias a[onclick*='eliminaReferencia']"));

  public static final Target BOTON_CONTINUAR =
      Target.the("botón Continuar").located(By.id("mySubmit_"));

  public static final Target ENCABEZADO_VALOR_A_PAGAR =
      Target.the("encabezado Valor a pagar")
          .located(
              By.xpath(
                  "//*[not(self::script) and not(self::style)"
                      + " and contains(normalize-space(.), 'Valor a pagar')"
                      + " and not(.//*[contains(normalize-space(.), 'Valor a pagar')])]"));

  public static final Target TITULO_DETALLE_DE_TU_PAGO =
      Target.the("título Detalle de tu pago").located(By.cssSelector("h2.labelEmp-new"));

  public static final Target REFERENCIA_DE_PAGO_EN_DETALLE =
      Target.the("referencia de pago {0} en Detalle de tu pago")
          .locatedBy("//label[contains(@class, 'etiquetas') and normalize-space(.)='{0}']");

  public static final Target SELECTOR_MEDIO_DE_PAGO =
      Target.the("selector de medio de pago (Elige una opción)")
          .located(
              By.xpath(
                  "//div[@id='select' and contains(@class, 'select')]"
                      + " | //*[self::div or self::button or self::select]"
                      + "[contains(normalize-space(.), 'Elige una opción')"
                      + " and not(.//*[contains(normalize-space(.), 'Elige una opción')])]"));

  public static final Target OPCION_MEDIO_PAGO_PSE_NEQUI_DAVIPLATA =
      Target.the("opción medio de pago PSE - NEQUI - DAVIPLATA")
          .located(
              By.xpath(
                  "//*[@id='opciones']//div[contains(@class, 'contenido-opcion')"
                      + " and .//img[contains(@src, 'PSE.png')]]"));

  public static final Target MEDIO_PAGO_PSE_SELECCIONADO =
      Target.the("medio de pago PSE - NEQUI - DAVIPLATA seleccionado")
          .located(By.cssSelector("#select img[src*='PSE.png']"));

  public static final Target INPUT_FORMA_PAGO =
      Target.the("campo oculto FORMA_PAGO").located(By.name("FORMA_PAGO"));

  public static final Target BOTON_CONFIRMAR_MEDIO_PAGO =
      Target.the("botón Confirmar medio de pago")
          .located(By.cssSelector("input#mySubmit_[value='Confirmar']"));

  private PaginaPortalPagosEmpresas() {}
}
