package evidencias;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public final class CapturaEscritorio {

  private static final int RETARDO_ESTABILIZACION_MILISEGUNDOS = 500;
  private static final int TIEMPO_ACTIVACION_SEGUNDOS = 5;

  private CapturaEscritorio() {}

  public static Evidencia capturar(String titulo, String tituloVentanaNavegador) {
    if (GraphicsEnvironment.isHeadless()) {
      throw new IllegalStateException(
          "La captura del escritorio requiere una sesión gráfica de Windows activa");
    }

    int numero = ContextoEvidencias.siguienteNumero();
    Path rutaImagen = ContextoEvidencias.rutaPara(numero, titulo);

    try {
      activarVentanaNavegador(tituloVentanaNavegador);
      Robot robot = new Robot();
      robot.waitForIdle();
      Toolkit.getDefaultToolkit().sync();
      robot.delay(RETARDO_ESTABILIZACION_MILISEGUNDOS);

      Rectangle escritorioCompleto = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage imagen = robot.createScreenCapture(escritorioCompleto);

      if (!ImageIO.write(imagen, "png", rutaImagen.toFile())) {
        throw new IOException("No existe un escritor disponible para imágenes PNG");
      }

      return ContextoEvidencias.registrar(numero, titulo, rutaImagen, LocalDateTime.now());
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "No fue posible capturar el escritorio completo para: " + titulo, exception);
    } catch (AWTException | IOException exception) {
      throw new IllegalStateException(
          "No fue posible capturar el escritorio completo para: " + titulo, exception);
    }
  }

  private static void activarVentanaNavegador(String tituloVentana)
      throws IOException, InterruptedException {
    if (tituloVentana == null || tituloVentana.trim().isEmpty()) {
      throw new IllegalStateException("El navegador no tiene un título disponible para activarlo");
    }

    String tituloBase64 =
        Base64.getEncoder().encodeToString(tituloVentana.getBytes(StandardCharsets.UTF_8));
    String script =
        "$titulo=[Text.Encoding]::UTF8.GetString([Convert]::FromBase64String('"
            + tituloBase64
            + "'));"
            + "$shell=New-Object -ComObject WScript.Shell;"
            + "if(-not $shell.AppActivate($titulo)){exit 2}";
    String powershell =
        System.getenv("WINDIR")
            + "\\System32\\WindowsPowerShell\\v1.0\\powershell.exe";

    Process proceso =
        new ProcessBuilder(
                powershell,
                "-NoProfile",
                "-NonInteractive",
                "-WindowStyle",
                "Hidden",
                "-Command",
                script)
            .start();

    if (!proceso.waitFor(TIEMPO_ACTIVACION_SEGUNDOS, TimeUnit.SECONDS)) {
      proceso.destroyForcibly();
      throw new IOException("La activación de la ventana del navegador excedió el tiempo límite");
    }
    if (proceso.exitValue() != 0) {
      throw new IOException("Windows no pudo activar la ventana del navegador controlado");
    }
  }

}
