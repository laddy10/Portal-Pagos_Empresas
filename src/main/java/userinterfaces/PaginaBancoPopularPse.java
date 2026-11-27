package userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class PaginaBancoPopularPse {

  public static final Target TITULO_PORTAL_BANCO =
      Target.the("título del portal Banco Popular").located(By.cssSelector("p.title-pse"));

  public static final Target CAMPO_NUMERO_DOCUMENTO =
      Target.the("campo Número de documento en Banco Popular")
          .located(By.cssSelector("input[formcontrolname='documentNumber']"));

  public static final Target CAMPO_CONTRASENA =
      Target.the("campo Contraseña en Banco Popular")
          .located(By.cssSelector("input[formcontrolname='password']"));

  public static final Target BOTON_INGRESAR =
      Target.the("botón Ingresar en Banco Popular")
          .located(By.xpath("//button[normalize-space(.)='Ingresar']"));

  private PaginaBancoPopularPse() {}
}
