package evidencias;

import java.nio.file.Path;
import java.time.LocalDateTime;

public final class Evidencia {

  private final int numero;
  private final String titulo;
  private final Path rutaImagen;
  private final LocalDateTime fechaHora;

  public Evidencia(int numero, String titulo, Path rutaImagen, LocalDateTime fechaHora) {
    this.numero = numero;
    this.titulo = titulo;
    this.rutaImagen = rutaImagen;
    this.fechaHora = fechaHora;
  }

  public int getNumero() {
    return numero;
  }

  public String getTitulo() {
    return titulo;
  }

  public Path getRutaImagen() {
    return rutaImagen;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }
}
