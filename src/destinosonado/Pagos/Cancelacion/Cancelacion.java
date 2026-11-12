package destinosonado.Pagos.Cancelacion;

import java.time.LocalDate;

public class Cancelacion implements java.io.Serializable {
    private LocalDate fecha;
    private String motivo;
    private double importeDevuelto;

    public Cancelacion(LocalDate fecha, String motivo, double importeDevuelto) {
        this.fecha = fecha;
        this.motivo = motivo;
        this.importeDevuelto = importeDevuelto;
    }

    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getImporteDevuelto() {
        return importeDevuelto;
    }

    public void setImporteDevuelto(double importeDevuelto) {
        this.importeDevuelto = importeDevuelto;
    }
}
