package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Path;
import java.util.Collections;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Chrome con un perfil persistente dedicado a la automatización.
 *
 * <p>Por defecto Selenium abre Chrome con un perfil temporal nuevo en cada ejecución (se comporta
 * como incógnito: sin cookies ni historial). Con un perfil fijo en disco las cookies de Google
 * (reCAPTCHA) y del portal se conservan entre ejecuciones, lo que aumenta la "confianza" que
 * reCAPTCHA asigna al navegador y suele permitir que el checkbox pase sin desafío de imágenes
 * después de haberlo resuelto manualmente una vez.
 *
 * <p>Ruta del perfil configurable con {@code -Dperfil.chrome.dir=...}. Por defecto: {@code
 * <proyecto>/perfiles/chrome-automatizacion}. No usar el perfil personal de Chrome: Chrome bloquea
 * un perfil que ya está abierto y la sesión de Selenium falla.
 */
public class ChromePerfilPersistente implements DriverSource {

  public static final String PROPIEDAD_DIRECTORIO_PERFIL = PerfilChrome.PROPIEDAD_DIRECTORIO;

  @Override
  public WebDriver newDriver() {
    WebDriverManager.chromedriver().setup();

    Path directorioPerfil = directorioPerfil();
    System.out.println("PERFIL_CHROME_PERSISTENTE=" + directorioPerfil);

    if (PerfilChrome.enUso(directorioPerfil)) {
      throw new IllegalStateException(
          "El perfil de Chrome está en uso (¿quedó abierto el Chrome de inicio de sesión?)."
              + " Ciérrelo con 'Cerrar y guardar sesión' o cierre esa ventana: "
              + directorioPerfil);
    }

    ChromeOptions options = new ChromeOptions();
    options.addArguments(
        "--user-data-dir=" + directorioPerfil,
        "--profile-directory=Default",
        "--start-maximized",
        "--disable-gpu",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        // Reduce señales de automatización que reCAPTCHA usa para puntuar al navegador.
        "--disable-blink-features=AutomationControlled",
        "--no-first-run",
        "--no-default-browser-check");
    options.setExperimentalOption(
        "excludeSwitches", Collections.singletonList("enable-automation"));
    options.setExperimentalOption("useAutomationExtension", false);

    return new ChromeDriver(options);
  }

  @Override
  public boolean takesScreenshots() {
    return true;
  }

  static Path directorioPerfil() {
    return PerfilChrome.directorio();
  }
}
