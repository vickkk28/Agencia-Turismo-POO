package destinosonado.Pagos.Cancelacion;

import java.time.LocalDate;

public class NotaAjuste implements java.io.Serializable {
    private LocalDate fecha;
    private String motivo;
    private double importe;

    public NotaAjuste(LocalDate fecha, String motivo, double importe) {
        this.fecha = fecha;
        this.motivo = motivo;
        this.importe = importe;
    }
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

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}

