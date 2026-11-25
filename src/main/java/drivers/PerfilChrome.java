package drivers;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/** Utilidades del perfil de Chrome que usa la automatización. */
public final class PerfilChrome {

  public static final String PROPIEDAD_DIRECTORIO = "perfil.chrome.dir";

  private PerfilChrome() {}

  /**
   * Carpeta del perfil: {@code -Dperfil.chrome.dir} o {@code
   * <proyecto>/perfiles/chrome-automatizacion}.
   */
  public static Path directorio() {
    String configurado = System.getProperty(PROPIEDAD_DIRECTORIO);
    Path ruta =
        configurado == null || configurado.trim().isEmpty()
            ? Paths.get(System.getProperty("user.dir"), "perfiles", "chrome-automatizacion")
            : Paths.get(configurado.trim());
    return ruta.toAbsolutePath().normalize();
  }

  /** Chrome mantiene bloqueado el archivo {@code lockfile} mientras usa el perfil. */
  public static boolean enUso(Path perfil) {
    Path lock = perfil.resolve("lockfile");
    if (!Files.exists(lock)) {
      return false;
    }
    try (FileChannel ignorado = FileChannel.open(lock, StandardOpenOption.WRITE)) {
      return false;
    } catch (IOException exception) {
      return true;
    }
  }
}
