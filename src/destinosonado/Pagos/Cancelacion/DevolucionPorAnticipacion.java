package destinosonado.Pagos.Cancelacion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Devolucion segun los dias que faltan para la salida:
 * 30 dias o mas: 90 %, de 29 a 15 dias: 50 %, de 14 a 7 dias: 20 %, menos de 7 dias: 0 %.
 */
public class DevolucionPorAnticipacion implements PoliticaDevolucion {

    @Override
    public double porcentajeDevolucion(LocalDate fechaCancelacion, LocalDate fechaSalida) {
        long dias = ChronoUnit.DAYS.between(fechaCancelacion, fechaSalida);
        if (dias >= 30) {
            return 0.90;
        }
        if (dias >= 15) {
            return 0.50;
        }
        if (dias >= 7) {
            return 0.20;
        }
        return 0.0;
    }
}
