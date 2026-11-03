package destinosonado;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import destinosonado.excepciones.CupoInsuficienteException;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.modelo.enums.EstadoReserva;
import destinosonado.modelo.enums.EstadoSalida;
import destinosonado.Empleados.Guia;
import destinosonado.tours.Tour;

/**
 * Realizacion concreta de un tour en una fecha. Es la experta en su propio
 * cupo: sabe cuantos lugares quedan y si admite nuevas reservas (patron Experto).
 * Las validaciones del guia y del vehiculo las hace quien programa la salida.
 */
public class Salida implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final Tour tour;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final int cupoMinimo;
    private final int cupoMaximo;
    private final Guia guia;
    private final Vehiculo vehiculo;
    private EstadoSalida estado;
    private int lugaresOcupados;
    private final List<Reserva> reservas = new ArrayList<>();

    public Salida(int id, Tour tour, LocalDate fechaInicio, Guia guia, Vehiculo vehiculo) {
        this.id = id;
        this.tour = tour;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaInicio.plusDays(tour.getDuracionDias() - 1L);
        this.cupoMinimo = tour.getCupoMinimo();
        this.cupoMaximo = tour.getCupoMaximo();
        this.guia = guia;
        this.vehiculo = vehiculo;
        this.estado = EstadoSalida.PROGRAMADA;
    }

    /** Una salida solo admite reservas si esta programada o confirmada. */
    public boolean admiteReservas() {
        return estado == EstadoSalida.PROGRAMADA || estado == EstadoSalida.CONFIRMADA;
    }

    public int lugaresDisponibles() {
        return cupoMaximo - lugaresOcupados;
    }

    /** Ocupa lugares del cupo. Al llegar al maximo la salida pasa a COMPLETA. */
    public void ocupar(int cantidad) throws ReglaDeNegocioException {
        if (cantidad <= 0) {
            throw new ReglaDeNegocioException("La cantidad de lugares a ocupar debe ser mayor a cero");
        }
        if (cantidad > lugaresDisponibles()) {
            throw new CupoInsuficienteException(cantidad, lugaresDisponibles());
        }
        lugaresOcupados += cantidad;
        if (lugaresDisponibles() == 0) {
            estado = EstadoSalida.COMPLETA;
        }
    }

    /** Devuelve lugares al cupo (por ejemplo, al anular una reserva). */
    public void liberar(int cantidad) {
        lugaresOcupados = Math.max(0, lugaresOcupados - cantidad);
        if (estado == EstadoSalida.COMPLETA && lugaresDisponibles() > 0) {
            estado = EstadoSalida.PROGRAMADA;
            confirmarSiCorresponde();
        }
    }

    /** Registra una reserva hecha para esta salida. */
    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    /** La salida se confirma al alcanzar el cupo minimo con reservas confirmadas. */
    public void confirmarSiCorresponde() {
        if (estado == EstadoSalida.PROGRAMADA && lugaresConfirmados() >= cupoMinimo) {
            estado = EstadoSalida.CONFIRMADA;
        }
    }

    public int lugaresConfirmados() {
        int total = 0;
        for (Reserva r : reservas) {
            if (r.getEstado() == EstadoReserva.CONFIRMADA) {
                total += r.cantidadPasajeros();
            }
        }
        return total;
    }

    /** Delegacion en el tour: cada categoria de tour valida a su manera (Polimorfismo). */
    public void validarPasajero(Pasajero pasajero) throws ReglaDeNegocioException {
        tour.validarPasajero(pasajero);
    }

    public boolean requiereSeguro() {
        return tour.requiereSeguro();
    }

    public double precioBasePorPasajero() {
        return tour.precioBase(fechaInicio);
    }

    /** Total vendido en servicios adicionales (base del honorario variable del guia). */
    public double ventasAdicionales() {
        double total = 0;
        for (Reserva r : reservas) {
            if (r.getEstado() != EstadoReserva.ANULADA) {
                total += r.totalServiciosAdicionales();
            }
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public Tour getTour() {
        return tour;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getCupoMinimo() {
        return cupoMinimo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public EstadoSalida getEstado() {
        return estado;
    }

    public Guia getGuia() {
        return guia;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }
}
