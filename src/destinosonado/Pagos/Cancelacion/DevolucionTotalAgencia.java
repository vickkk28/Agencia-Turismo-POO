package destinosonado.Pagos.Cancelacion;

import java.time.LocalDate;

/** Si la cancelacion la provoca la agencia, se devuelve siempre el 100 % de lo pagado. */
public class DevolucionTotalAgencia implements PoliticaDevolucion {

    @Override
    public double porcentajeDevolucion(LocalDate fechaCancelacion, LocalDate fechaSalida) {
        return 1.0;
    }
}
