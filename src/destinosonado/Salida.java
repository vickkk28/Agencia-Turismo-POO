package destinosonado;

import destinosonado.modelo.enums.EstadoSalida;

import java.time.LocalDate;

public class Salida {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupoMinimo;
    private int cupoMaximo;
    private EstadoSalida estado;

    public Salida(LocalDate fechaInicio, LocalDate fechaFin, int cupoMinimo, int cupoMaximo, EstadoSalida estado) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupoMinimo = cupoMinimo;
        this.cupoMaximo = cupoMaximo;
        this.estado = estado;
    }

    public boolean admitirReservas() {
        return true;
    }

    public int lugaresDisponibles() {
        return this.cupoMaximo;
    }

    public void ocupar(int cantidad) {
            // Lógica para ocupar lugares
    }

    public void liberar(int cantidad) {
            // Lógica para liberar lugares
    }

    public void validarPasajero(Pasajero pasajero) {
            // Validación del pasajero para esta salida
    }

    public boolean requiereSeguro() {
        return false;
    }

    public double precioBasePorPasajero() {
        return 0.0;
    }

    public double ventasAdicionales() {
        return 0.0;
    }

    public void confirmarSiCorresponde() {
            // Lógica de confirmación
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCupoMinimo() {
        return cupoMinimo;
    }

    public void setCupoMinimo(int cupoMinimo) {
        this.cupoMinimo = cupoMinimo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public EstadoSalida getEstado() {
        return estado;
    }

    public void setEstado(EstadoSalida estado) {
        this.estado = estado;
    }
}