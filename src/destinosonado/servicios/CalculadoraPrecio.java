package destinosonado.servicios;

import java.util.ArrayList;
import java.util.List;

import destinosonado.Pasajero;
import destinosonado.Reserva;

/**
 * Calcula el total de una reserva. Es una clase de "fabricacion pura": no
 * representa un concepto del negocio, pero concentra el calculo para que
 * Reserva no tenga esa responsabilidad (SRP).
 *
 * Total = tarifas segun tipo de pasajero, menos la promocion mas favorable
 * (las promociones no se acumulan), mas los servicios adicionales.
 */
public class CalculadoraPrecio {

    private final List<Promocion> promociones;

    public CalculadoraPrecio() {
        this(List.of(new PromocionReservaAnticipada(), new PromocionGrupo()));
    }

    public CalculadoraPrecio(List<Promocion> promociones) {
        this.promociones = new ArrayList<>(promociones);
    }

    public double calcularTotal(Reserva reserva) {
        double tarifaBase = reserva.getSalida().precioBasePorPasajero();
        double tarifas = 0;
        for (Pasajero p : reserva.getPasajeros()) {
            tarifas += tarifaBase * p.getTipo().factorTarifa();
        }
        double descuento = mejorDescuento(reserva);
        return tarifas * (1 - descuento) + reserva.totalServiciosAdicionales();
    }

    /** Devuelve el mayor descuento entre todas las promociones (la mas favorable al cliente). */
    public double mejorDescuento(Reserva reserva) {
        double mejor = 0.0;
        for (Promocion promocion : promociones) {
            mejor = Math.max(mejor, promocion.porcentajeDescuento(reserva));
        }
        return mejor;
    }
}
