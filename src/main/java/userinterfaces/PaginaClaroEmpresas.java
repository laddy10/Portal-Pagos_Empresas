package userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class PaginaClaroEmpresas {

  public static final String URL =
      "https://empresaspre.portalpagospre.claro.com.co/phrame.php?action=despliegue_personal&clase=vistasclaro&metodo=pantalla_inicio&empresa=claro&segmento=empresas";

  public static final Target CONTENIDO_PRINCIPAL =
      Target.the("contenido principal del portal Claro Empresas")
          .located(By.cssSelector("main#cPal"));

  public static final Target SECCION_PAGOS_Y_RECARGAS =
      Target.the("sección Pagos y Recargas").located(By.cssSelector("section.ce04Wrap"));

  public static final Target PAGUE_Y_RECARGUE_AQUI =
      Target.the("enlace Pague y recargue aquí")
          .located(
              By.cssSelector(
                  "section.ce04Wrap a[href*='portalpagosempresa.claro.com.co']"
                      + "[href*='segmento=empresas']"));

  private PaginaClaroEmpresas() {}
}
