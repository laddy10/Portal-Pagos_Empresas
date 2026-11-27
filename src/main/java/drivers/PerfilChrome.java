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
   * %LOCALAPPDATA%\PortalPagosEmpresas\chrome-automatizacion}.
   *
   * <p>Va fuera del proyecto a propósito: si el proyecto está en OneDrive, la sincronización
   * bloquearía o dañaría los archivos del perfil mientras Chrome los usa.
   */
  public static Path directorio() {
    String configurado = System.getProperty(PROPIEDAD_DIRECTORIO);
    if (configurado != null && !configurado.trim().isEmpty()) {
      return Paths.get(configurado.trim()).toAbsolutePath().normalize();
    }
    String localAppData = System.getenv("LOCALAPPDATA");
    Path base =
        localAppData == null || localAppData.trim().isEmpty()
            ? Paths.get(System.getProperty("user.home"), "AppData", "Local")
            : Paths.get(localAppData);
    return base.resolve("PortalPagosEmpresas")
        .resolve("chrome-automatizacion")
        .toAbsolutePath()
        .normalize();
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
