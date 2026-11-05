package destinosonado.reservas;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import destinosonado.DatosPasajero;
import destinosonado.Pagos.PagoEfectivo;
import destinosonado.Reserva;
import destinosonado.Salida;
import destinosonado.ServicioAdicional;
import destinosonado.Pagos.Cancelacion.Cancelacion;
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

    private static PagoEfectivo efectivo(double importe) {
        return new PagoEfectivo(new Date(), importe);
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

        reserva.registrarPago(efectivo(200));
        Verificador.igualTexto("con el 20 % pagado sigue PENDIENTE", EstadoReserva.PENDIENTE, reserva.getEstado());

        reserva.registrarPago(efectivo(100));
        Verificador.igualTexto("con la senia del 30 % pasa a CONFIRMADA", EstadoReserva.CONFIRMADA, reserva.getEstado());
        Verificador.igual("el saldo es 700", 700, reserva.saldo());

        Verificador.lanza("un pago mayor al saldo lanza ReglaDeNegocio", ReglaDeNegocioException.class,
                () -> reserva.registrarPago(efectivo(800)));
    }

    private static void cancelacion() throws Exception {
        Salida salida = new Salida(2, new TourDePrueba(2, 10, 1000, false),
                LocalDate.of(2027, 3, 1), null, null);
        Reserva reserva = nuevaReserva(salida);
        reserva.agregarPasajero(adulto("Beto"));
        reserva.setTotal(1000);
        salida.ocupar(1);
        reserva.registrarPago(efectivo(300));

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

        Reserva sinSeguro = nuevaReserva(salida);
        Verificador.lanza("un tour de aventura rechaza al pasajero sin seguro",
                ReglaDeNegocioException.class, () -> sinSeguro.agregarPasajero(adulto("Carla")));
        Verificador.igual("el pasajero rechazado no queda en la reserva", 0, sinSeguro.cantidadPasajeros());

        Reserva conSeguro = nuevaReserva(salida);
        conSeguro.agregarPasajero(new DatosPasajero("Diego", "Prueba", "30111222", "11-1111-1111",
                LocalDate.of(1990, 5, 1), TipoPasajero.ADULTO, "11-5555-0000",
                List.of(new ServicioAdicional("Seguro de viaje", 50, true))));
        conSeguro.validarServiciosObligatorios();
        Verificador.igual("con el seguro contratado el pasajero se acepta", 1, conSeguro.cantidadPasajeros());
    }
}
