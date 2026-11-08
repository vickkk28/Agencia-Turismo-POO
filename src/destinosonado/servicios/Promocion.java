package destinosonado.servicios;

import destinosonado.model.Reserva;

/**
 * Promocion aplicable a una reserva. Agregar una promocion nueva consiste en
 * crear una clase que implemente esta interfaz, sin tocar la calculadora (OCP).
 */
public interface Promocion {

    /** Devuelve el descuento como fraccion entre 0 y 1 (0,15 = 15 %). */
    double porcentajeDescuento(Reserva reserva);
}