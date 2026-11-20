# PortalPagosEmpresas

Esqueleto de automatización exclusivamente WEB con Java 11, Gradle 7.4, Serenity BDD,
Cucumber, Selenium, JUnit 4 y el patrón Screenplay.

## Ejecución

Chrome y un ChromeDriver compatible deben estar disponibles en el servidor. El driver puede
configurarse mediante `webdriver.chrome.driver` o incorporarse al `PATH` del proceso.

```powershell
.\gradlew.bat clean test aggregate
```

El reporte se genera en `target/site/serenity/index.html`.
# Portal-Pagos_Empresas
