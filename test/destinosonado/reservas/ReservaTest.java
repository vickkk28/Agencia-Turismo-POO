package destinosonado.reservas;

import java.time.LocalDate;

import destinosonado.DatosPasajero;
import destinosonado.Pagos.PagoEfectivo;
import destinosonado.Reserva;
import destinosonado.Salida;
import destinosonado.Cancelacion;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.modelo.enums.EstadoReserva;
import destinosonado.modelo.enums.TipoPasajero;

public class ReservaTest {

    private static DatosPasajero adulto(String nombre) {
        return new DatosPasajero(nombre, "Prueba", "30111222", "11-1111-1111",
                LocalDate.of(1990, 5, 1), TipoPasajero.ADULTO, "11-5555-0000");
    }

    private static Reserva nuevaReserva(Salida salida) {
        return new Reserva(1, LocalDate.of(2026, 11, 1), null, null, salida);
    }

    public static void main(String[] args) throws Exception {
        pagosYConfirmacion();
        cancelacion();
        seguroObligatorio();
        Verificador.resumen();
    }

    private static void pagosYConfirmacion() throws Exception {
        Salida salida = new Salida(1, new TourDePrueba(2, 10, 1000, false),
                LocalDate.of(2027, 3, 1), null, null);
        Reserva reserva = nuevaReserva(salida);
        reserva.agregarPasajero(adulto("Ana"));
        reserva.setTotal(1000);

        Verificador.igual("la reserva tiene un pasajero", 1, reserva.cantidadPasajeros());
        Verificador.igualTexto("una reserva nueva esta PENDIENTE", EstadoReserva.PENDIENTE, reserva.getEstado());

        reserva.registrarPago(new PagoEfectivo(LocalDate.of(2026, 11, 2), 200));
        Verificador.igualTexto("con el 20 % pagado sigue PENDIENTE", EstadoReserva.PENDIENTE, reserva.getEstado());

        reserva.registrarPago(new PagoEfectivo(LocalDate.of(2026, 11, 3), 100));
        Verificador.igualTexto("con la senia del 30 % pasa a CONFIRMADA", EstadoReserva.CONFIRMADA, reserva.getEstado());
        Verificador.igual("el saldo es 700", 700, reserva.saldo());

        Verificador.lanza("un pago mayor al saldo lanza ReglaDeNegocio", ReglaDeNegocioException.class,
                () -> reserva.registrarPago(new PagoEfectivo(LocalDate.of(2026, 11, 4), 800)));
    }

    private static void cancelacion() throws Exception {
        Salida salida = new Salida(2, new TourDePrueba(2, 10, 1000, false),
                LocalDate.of(2027, 3, 1), null, null);
        Reserva reserva = nuevaReserva(salida);
        reserva.agregarPasajero(adulto("Beto"));
        reserva.setTotal(1000);
        salida.ocupar(1);
        reserva.registrarPago(new PagoEfectivo(LocalDate.of(2026, 11, 2), 300));

        Cancelacion cancelacion = reserva.cancelar("el cliente desistio", 0.5);
        Verificador.igual("se devuelve la mitad de lo pagado", 150, cancelacion.getImporteDevuelto());
        Verificador.igualTexto("la reserva cancelada queda ANULADA", EstadoReserva.ANULADA, reserva.getEstado());
        Verificador.igual("se liberan los lugares de la salida", 10, salida.lugaresDisponibles());
        Verificador.verdadero("la reserva anulada se conserva con su cancelacion", reserva.getCancelacion() != null);
        Verificador.lanza("no se puede cancelar dos veces", ReglaDeNegocioException.class,
                () -> reserva.cancelar("otra vez", 1.0));
    }

    private static void seguroObligatorio() throws Exception {
        Salida salida = new Salida(3, new TourDePrueba(2, 10, 1000, true),
                LocalDate.of(2027, 3, 1), null, null);
        Reserva reserva = nuevaReserva(salida);
        reserva.agregarPasajero(adulto("Carla"));

        Verificador.lanza("un tour que exige seguro rechaza al pasajero sin seguro",
                ReglaDeNegocioException.class, () -> reserva.validarServiciosObligatorios());
    }
}
