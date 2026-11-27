package userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class PaginaFormularioPagoPse {

  public static final Target SELECT_BANCO = Target.the("selector de Banco").located(By.id("BANCO"));

  public static final Target CAMPO_NOMBRE_TITULAR =
      Target.the("campo Nombre del Titular").located(By.id("TITULAR"));

  public static final Target SELECT_TIPO_CLIENTE =
      Target.the("selector Tipo de Cliente").located(By.id("TIPO_CLIENTE"));

  public static final Target SELECT_TIPO_DOCUMENTO =
      Target.the("selector Documento de Identificación").located(By.id("TIPO_DOCUMENTO"));

  public static final Target CAMPO_NUMERO_DOCUMENTO =
      Target.the("campo Número Documento")
          .located(
              By.xpath(
                  "//*[normalize-space(.)='Número Documento']"
                      + "/following::input[@type='text' or @type='tel' or @type='number' or not(@type)][1]"));

  public static final Target CAMPO_NUMERO_TELEFONO =
      Target.the("campo Número Teléfono")
          .located(
              By.xpath(
                  "//*[normalize-space(.)='Número Teléfono']"
                      + "/following::input[@type='text' or @type='tel' or @type='number' or not(@type)][1]"));

  public static final Target CAMPO_CORREO_ELECTRONICO =
      Target.the("campo Correo Electrónico").located(By.id("EMAIL"));

  public static final Target BOTON_CONFIRMAR =
      Target.the("botón Confirmar formulario PSE")
          .located(
              By.xpath(
                  "//*[(self::button or self::a) and normalize-space(.)='Confirmar']"
                      + " | //input[(@type='submit' or @type='button') and @value='Confirmar']"));

  private PaginaFormularioPagoPse() {}
}
