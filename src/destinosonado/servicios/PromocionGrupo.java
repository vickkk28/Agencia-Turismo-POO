package destinosonado.servicios;

import destinosonado.model.Reserva;

/** 10 % de descuento para reservas de 6 o mas pasajeros. */
public class PromocionGrupo implements Promocion {

    private static final int PASAJEROS_MINIMOS = 6;
    private static final double DESCUENTO = 0.10;

    @Override
    public double porcentajeDescuento(Reserva reserva) {
        return reserva.cantidadPasajeros() >= PASAJEROS_MINIMOS ? DESCUENTO : 0.0;
    }
}
