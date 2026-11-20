package evidencias;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ContextoEvidencias {

  private static final DateTimeFormatter FORMATO_DIRECTORIO =
      DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
  private static final ThreadLocal<EstadoEscenario> ESTADO = new ThreadLocal<>();

  private ContextoEvidencias() {}

  public static void iniciar(String escenario) throws IOException {
    LocalDateTime inicio = LocalDateTime.now();
    String nombreSeguro = normalizarNombre(escenario);
    Path directorio =
        Paths.get(
            System.getProperty("user.dir"),
            "target",
            "evidencias",
            nombreSeguro + "-" + FORMATO_DIRECTORIO.format(inicio));

    Files.createDirectories(directorio);
    ESTADO.set(new EstadoEscenario(escenario, inicio, directorio));
  }

  public static boolean estaActivo() {
    return ESTADO.get() != null;
  }

  public static String escenario() {
    return estadoActual().escenario;
  }

  public static LocalDateTime inicio() {
    return estadoActual().inicio;
  }

  public static Path directorio() {
    return estadoActual().directorio;
  }

  public static int siguienteNumero() {
    return estadoActual().evidencias.size() + 1;
  }

  public static Path rutaPara(int numero, String titulo) {
    String nombre = String.format("%02d-%s.png", numero, normalizarNombre(titulo));
    return estadoActual().directorio.resolve(nombre);
  }

  public static Evidencia registrar(
      int numero, String titulo, Path rutaImagen, LocalDateTime fechaHora) {
    Evidencia evidencia = new Evidencia(numero, titulo, rutaImagen, fechaHora);
    estadoActual().evidencias.add(evidencia);
    return evidencia;
  }

  public static List<Evidencia> evidencias() {
    List<Evidencia> evidencias = new ArrayList<>(estadoActual().evidencias);
    evidencias.sort(Comparator.comparingInt(Evidencia::getNumero));
    return evidencias;
  }

  public static void finalizar() {
    ESTADO.remove();
  }

  private static EstadoEscenario estadoActual() {
    EstadoEscenario estado = ESTADO.get();
    if (estado == null) {
      throw new IllegalStateException("No existe un contexto activo para capturar evidencias");
    }
    return estado;
  }

  private static String normalizarNombre(String valor) {
    String sinAcentos = Normalizer.normalize(valor, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
    String nombre = sinAcentos.replaceAll("[^A-Za-z0-9]+", "-").replaceAll("(^-|-$)", "");
    return nombre.isEmpty() ? "evidencia" : nombre.toLowerCase();
  }

  private static final class EstadoEscenario {

    private final String escenario;
    private final LocalDateTime inicio;
    private final Path directorio;
    private final List<Evidencia> evidencias = new ArrayList<>();

    private EstadoEscenario(String escenario, LocalDateTime inicio, Path directorio) {
      this.escenario = escenario;
      this.inicio = inicio;
      this.directorio = directorio;
    }
  }
}
