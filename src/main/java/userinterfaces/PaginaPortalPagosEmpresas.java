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

  private PaginaPortalPagosEmpresas() {}
}
