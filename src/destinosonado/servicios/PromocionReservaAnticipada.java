package destinosonado.servicios;

import java.time.temporal.ChronoUnit;

import destinosonado.Reserva;

/** 15 % de descuento si la reserva se hace con mas de 60 dias de anticipacion. */
public class PromocionReservaAnticipada implements Promocion {

    private static final long DIAS_MINIMOS = 60;
    private static final double DESCUENTO = 0.15;

    @Override
    public double porcentajeDescuento(Reserva reserva) {
        long dias = ChronoUnit.DAYS.between(reserva.getFecha(), reserva.fechaSalida());
        return dias > DIAS_MINIMOS ? DESCUENTO : 0.0;
    }
}
