package destinosonado;

import java.time.LocalDate;

public class Liquidacion {
    private LocalDate fecha;
    private int diasTrabajados;
    private double monto;

    public Liquidacion(LocalDate fecha, int diasTrabajados, double monto) {
        this.fecha = fecha;
        this.diasTrabajados = diasTrabajados;
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getDiasTrabajados() {
        return diasTrabajados;
    }

    public double getMonto() {
        return monto;
    }
}
