package stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import evidencias.ContextoEvidencias;
import evidencias.Evidencia;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import reportes.GeneradorReporteWord;

public class ReporteEvidenciasHooks {

  private static final String ETIQUETA =
      "@seleccion_pago_soluciones_fijas or @ingreso_numero_cuenta_soluciones_fijas";
  private static final String LINEA = "NO APLICA";
  private static final DateTimeFormatter FORMATO_FECHA =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  private static final Path DIRECTORIO_EVIDENCIAS_BASE =
      Paths.get(System.getProperty("user.dir"), "target", "evidencias")
          .toAbsolutePath()
          .normalize();

  @Before(value = ETIQUETA, order = 0)
  public void iniciarReporte(Scenario escenario) throws Exception {
    ContextoEvidencias.iniciar(escenario.getName());
  }

  @After(value = ETIQUETA, order = 0)
  public void generarReporte(Scenario escenario) {
    if (!ContextoEvidencias.estaActivo()) {
      return;
    }

    try {
      LocalDateTime inicio = ContextoEvidencias.inicio();
      String conclusion =
          escenario.isFailed()
              ? "La prueba finalizó con errores."
              : "La prueba finalizó exitosamente.";
      String duracion = formatearDuracion(Duration.between(inicio, LocalDateTime.now()));
      List<Evidencia> evidencias = ContextoEvidencias.evidencias();
      Path directorioEvidencias = ContextoEvidencias.directorio();

      Path reporte =
          GeneradorReporteWord.generar(
              ContextoEvidencias.escenario(),
              FORMATO_FECHA.format(inicio),
              conclusion,
              LINEA,
              duracion,
              evidencias);

      System.out.println("REPORTE_WORD_GENERADO=" + reporte.toAbsolutePath());

      if (reporteContieneLasEvidencias(reporte, evidencias.size())) {
        eliminarCarpetaDeEvidenciasTemporales(directorioEvidencias);
      } else {
        System.out.println(
            "ADVERTENCIA: se conservan las imágenes temporales porque el reporte Word no pudo"
                + " validarse: "
                + reporte.toAbsolutePath());
      }
    } catch (Exception exception) {
      throw new AssertionError("No fue posible generar el reporte Word de evidencias", exception);
    } finally {
      ContextoEvidencias.finalizar();
    }
  }

  private static boolean reporteContieneLasEvidencias(Path reporte, int cantidadEsperada) {
    try {
      if (!Files.exists(reporte) || Files.size(reporte) == 0) {
        return false;
      }
      try (InputStream entrada = Files.newInputStream(reporte);
          XWPFDocument documento = new XWPFDocument(entrada)) {
        return documento.getAllPictures().size() == cantidadEsperada;
      }
    } catch (IOException | RuntimeException exception) {
      return false;
    }
  }

  private static void eliminarCarpetaDeEvidenciasTemporales(Path directorioEvidencias) {
    Path directorioNormalizado = directorioEvidencias.toAbsolutePath().normalize();
    boolean esSubcarpetaConcretaValida =
        directorioNormalizado.startsWith(DIRECTORIO_EVIDENCIAS_BASE)
            && !directorioNormalizado.equals(DIRECTORIO_EVIDENCIAS_BASE);

    if (!esSubcarpetaConcretaValida) {
      System.out.println(
          "ADVERTENCIA: se omitió la limpieza porque la ruta no es una subcarpeta válida de"
              + " target/evidencias: "
              + directorioNormalizado);
      return;
    }

    try {
      eliminarDirectorioRecursivamente(directorioNormalizado);
      System.out.println("CARPETA_EVIDENCIAS_TEMPORAL_ELIMINADA=" + directorioNormalizado);
    } catch (IOException | RuntimeException exception) {
      System.out.println(
          "ADVERTENCIA: no fue posible eliminar la carpeta temporal de evidencias "
              + directorioNormalizado
              + ": "
              + exception.getMessage());
    }
  }

  private static void eliminarDirectorioRecursivamente(Path directorio) throws IOException {
    try (Stream<Path> rutas = Files.walk(directorio)) {
      rutas
          .sorted(Comparator.reverseOrder())
          .forEach(
              ruta -> {
                try {
                  Files.delete(ruta);
                } catch (IOException exception) {
                  throw new UncheckedIOException(exception);
                }
              });
    }
  }

  private static String formatearDuracion(Duration duracion) {
    long segundosTotales = Math.max(0, duracion.getSeconds());
    long horas = segundosTotales / 3600;
    long minutos = (segundosTotales % 3600) / 60;
    long segundos = segundosTotales % 60;
    return String.format("%02d:%02d:%02d", horas, minutos, segundos);
  }
}
