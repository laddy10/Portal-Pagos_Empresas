package reportes;

import evidencias.Evidencia;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public final class GeneradorReporteWord {

  private static final String PLANTILLA = "plantillas/plantilla_reporte_evidencias.docx";
  private static final DateTimeFormatter FORMATO_ARCHIVO =
      DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
  private static final double ANCHO_MAXIMO_IMAGEN_PUNTOS = 440.0;

  private GeneradorReporteWord() {}

  public static Path generar(
      String escenario,
      String fecha,
      String conclusion,
      String linea,
      String duracion,
      List<Evidencia> evidencias)
      throws IOException, InvalidFormatException {

    Path directorioReportes = Paths.get(System.getProperty("user.dir"), "target", "reportes");
    Files.createDirectories(directorioReportes);

    Path reporte =
        directorioReportes.resolve(
            "reporte-"
                + normalizarNombre(escenario)
                + "-"
                + FORMATO_ARCHIVO.format(LocalDateTime.now())
                + ".docx");

    ClassLoader classLoader = GeneradorReporteWord.class.getClassLoader();
    try (InputStream entrada = classLoader.getResourceAsStream(PLANTILLA)) {
      if (entrada == null) {
        throw new IOException("No se encontró la plantilla Word: " + PLANTILLA);
      }

      try (XWPFDocument documento = new XWPFDocument(entrada);
          FileOutputStream salida = new FileOutputStream(reporte.toFile())) {
        reemplazarMarcador(documento, "{{ESCENARIO}}", escenario);
        reemplazarMarcador(documento, "{{FECHA}}", fecha);
        reemplazarMarcador(documento, "{{CONCLUSION}}", conclusion);
        reemplazarMarcador(documento, "{{LINEA}}", linea);
        reemplazarMarcador(documento, "{{DURACION}}", duracion);

        verificarMarcadoresReemplazados(documento);
        agregarEvidencias(documento, evidencias);
        documento.write(salida);
      }
    }

    return reporte;
  }

  private static void agregarEvidencias(XWPFDocument documento, List<Evidencia> evidencias)
      throws IOException, InvalidFormatException {
    List<Evidencia> ordenadas = new ArrayList<>(evidencias);
    ordenadas.sort(Comparator.comparingInt(Evidencia::getNumero));

    XWPFParagraph encabezado = documento.createParagraph();
    encabezado.setPageBreak(true);
    encabezado.setSpacingAfter(180);
    XWPFRun encabezadoRun = encabezado.createRun();
    encabezadoRun.setBold(true);
    encabezadoRun.setFontSize(16);
    encabezadoRun.setText("EVIDENCIAS");

    for (int indice = 0; indice < ordenadas.size(); indice++) {
      Evidencia evidencia = ordenadas.get(indice);

      XWPFParagraph titulo = documento.createParagraph();
      if (indice > 0) {
        titulo.setPageBreak(true);
      }
      titulo.setSpacingAfter(120);
      XWPFRun tituloRun = titulo.createRun();
      tituloRun.setBold(true);
      tituloRun.setFontSize(11);
      tituloRun.setText("Evidencia " + evidencia.getNumero() + " - " + evidencia.getTitulo());

      agregarImagen(documento, evidencia);
    }
  }

  private static void agregarImagen(XWPFDocument documento, Evidencia evidencia)
      throws IOException, InvalidFormatException {
    BufferedImage imagen = ImageIO.read(evidencia.getRutaImagen().toFile());
    if (imagen == null) {
      throw new IOException("No se pudo leer la evidencia: " + evidencia.getRutaImagen());
    }

    double proporcion = (double) imagen.getHeight() / imagen.getWidth();
    double altoPuntos = ANCHO_MAXIMO_IMAGEN_PUNTOS * proporcion;

    XWPFParagraph parrafoImagen = documento.createParagraph();
    parrafoImagen.setAlignment(ParagraphAlignment.CENTER);
    XWPFRun imagenRun = parrafoImagen.createRun();

    try (InputStream entradaImagen = Files.newInputStream(evidencia.getRutaImagen())) {
      imagenRun.addPicture(
          entradaImagen,
          Document.PICTURE_TYPE_PNG,
          evidencia.getRutaImagen().getFileName().toString(),
          Units.toEMU(ANCHO_MAXIMO_IMAGEN_PUNTOS),
          Units.toEMU(altoPuntos));
    }
  }

  private static void reemplazarMarcador(XWPFDocument documento, String marcador, String valor) {
    for (XWPFParagraph parrafo : documento.getParagraphs()) {
      reemplazarEnParrafo(parrafo, marcador, valor);
    }
    for (XWPFTable tabla : documento.getTables()) {
      reemplazarEnTabla(tabla, marcador, valor);
    }
  }

  private static void reemplazarEnTabla(XWPFTable tabla, String marcador, String valor) {
    for (XWPFTableRow fila : tabla.getRows()) {
      for (XWPFTableCell celda : fila.getTableCells()) {
        for (XWPFParagraph parrafo : celda.getParagraphs()) {
          reemplazarEnParrafo(parrafo, marcador, valor);
        }
        for (XWPFTable tablaAnidada : celda.getTables()) {
          reemplazarEnTabla(tablaAnidada, marcador, valor);
        }
      }
    }
  }

  private static void reemplazarEnParrafo(XWPFParagraph parrafo, String marcador, String valor) {
    while (true) {
      List<XWPFRun> runs = parrafo.getRuns();
      String textoCompleto = textoCompleto(runs);
      int inicioMarcador = textoCompleto.indexOf(marcador);
      if (inicioMarcador < 0) {
        return;
      }

      int finMarcador = inicioMarcador + marcador.length();
      int posicion = 0;
      boolean valorInsertado = false;

      for (XWPFRun run : runs) {
        String textoRun = textoRun(run);
        int inicioRun = posicion;
        int finRun = inicioRun + textoRun.length();

        if (finRun > inicioMarcador && inicioRun < finMarcador) {
          int corteInicial = Math.max(0, inicioMarcador - inicioRun);
          int corteFinal = Math.min(textoRun.length(), finMarcador - inicioRun);
          String prefijo = textoRun.substring(0, corteInicial);
          String sufijo = textoRun.substring(corteFinal);

          if (!valorInsertado) {
            establecerTexto(run, prefijo + valor + (finMarcador <= finRun ? sufijo : ""));
            valorInsertado = true;
          } else {
            establecerTexto(run, finMarcador <= finRun ? sufijo : "");
          }
        }
        posicion = finRun;
      }
    }
  }

  private static String textoCompleto(List<XWPFRun> runs) {
    StringBuilder texto = new StringBuilder();
    for (XWPFRun run : runs) {
      texto.append(textoRun(run));
    }
    return texto.toString();
  }

  private static String textoRun(XWPFRun run) {
    StringBuilder texto = new StringBuilder();
    int cantidadTextos = run.getCTR().sizeOfTArray();
    for (int indice = 0; indice < cantidadTextos; indice++) {
      String fragmento = run.getCTR().getTArray(indice).getStringValue();
      if (fragmento != null) {
        texto.append(fragmento);
      }
    }
    return texto.toString();
  }

  private static void establecerTexto(XWPFRun run, String texto) {
    int cantidadTextos = run.getCTR().sizeOfTArray();
    if (cantidadTextos == 0) {
      run.setText(texto);
      return;
    }

    run.getCTR().getTArray(0).setStringValue(texto);
    for (int indice = cantidadTextos - 1; indice > 0; indice--) {
      run.getCTR().removeT(indice);
    }
  }

  private static void verificarMarcadoresReemplazados(XWPFDocument documento) {
    String xmlActualizado = documento.getDocument().xmlText();
    if (xmlActualizado.matches("(?s).*\\{\\{[^}]+}}.*")) {
      throw new IllegalStateException("El reporte conserva placeholders sin reemplazar");
    }
  }

  private static String normalizarNombre(String valor) {
    String sinAcentos = Normalizer.normalize(valor, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
    String nombre = sinAcentos.replaceAll("[^A-Za-z0-9]+", "-").replaceAll("(^-|-$)", "");
    return nombre.isEmpty() ? "escenario" : nombre.toLowerCase();
  }
}
