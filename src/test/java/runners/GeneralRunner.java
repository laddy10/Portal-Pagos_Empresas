package runners;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Runner general. Cambie el valor de {@code tags} para elegir qué escenarios ejecutar.
 *
 * <p>Ejemplos:
 *
 * <ul>
 *   <li>Un escenario: {@code "@recaptcha_sesion_persistente"}
 *   <li>Varios: {@code "@pagos_y_recargas or @seleccion_pago_soluciones_fijas"}
 *   <li>Toda la característica: {@code "@portal_claro_empresas"}
 *   <li>Excluir uno: {@code "@portal_claro_empresas and not @clic_recaptcha_firefox_soluciones_fijas"}
 * </ul>
 *
 * <p>Tags disponibles (ver src/test/resources/features):
 *
 * <ul>
 *   <li>@infraestructura
 *   <li>@portal_claro_empresas
 *   <li>@pagos_y_recargas
 *   <li>@seleccion_pago_soluciones_fijas
 *   <li>@ingreso_numero_cuenta_soluciones_fijas
 *   <li>@recaptcha_sesion_persistente
 *   <li>@clic_recaptcha_edge_soluciones_fijas
 *   <li>@clic_recaptcha_firefox_soluciones_fijas
 * </ul>
 *
 * <p>Ejecutar: clic derecho sobre esta clase en el IDE &gt; Run, o por consola: {@code .\gradlew.bat
 * clean test --tests runners.GeneralRunner}
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "stepDefinitions",
    tags = {"@recaptcha_sesion_persistente"},
    plugin = {"pretty"})
public class GeneralRunner {}
