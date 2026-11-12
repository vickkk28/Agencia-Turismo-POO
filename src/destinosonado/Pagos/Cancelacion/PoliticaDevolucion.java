package destinosonado.Pagos.Cancelacion;

import java.time.LocalDate;

/**
 * Estrategia para decidir cuanto se devuelve al cancelar una reserva.
 * Cada politica es una clase aparte: se puede agregar una nueva sin tocar
 * la reserva ni el controlador (principio abierto/cerrado).
 */
public interface PoliticaDevolucion {

    /** Porcentaje a devolver de lo pagado, entre 0 y 1. */
    double porcentajeDevolucion(LocalDate fechaCancelacion, LocalDate fechaSalida);
}
