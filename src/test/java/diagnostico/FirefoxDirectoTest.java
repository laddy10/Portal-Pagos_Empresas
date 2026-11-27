package diagnostico;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FirefoxDirectoTest {

  private static final String FIREFOX_BINARY = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

  @Test
  public void abrirFirefoxDirectamenteConSelenium() {
    WebDriver driver = null;

    try {
      WebDriverManager.firefoxdriver().setup();

      FirefoxOptions options = new FirefoxOptions();
      options.setBinary(new FirefoxBinary(new File(FIREFOX_BINARY)));

      driver = new FirefoxDriver(options);
      driver.get("https://www.google.com");

      Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
      System.out.println("browserName: " + capabilities.getBrowserName());
      System.out.println("browserVersion: " + capabilities.getVersion());

      Object geckoDriverVersion = capabilities.getCapability("moz:geckodriverVersion");
      if (geckoDriverVersion == null) {
        geckoDriverVersion = capabilities.getCapability("geckodriverVersion");
      }

      System.out.println(
          "driverVersion: " + (geckoDriverVersion != null ? geckoDriverVersion : "no disponible"));
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }
}
