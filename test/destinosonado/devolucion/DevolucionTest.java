package destinosonado.devolucion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import destinosonado.DatosPasajero;
import destinosonado.Pagos.Cancelacion.Cancelacion;
import destinosonado.Pagos.Cancelacion.DevolucionPorAnticipacion;
import destinosonado.Pagos.Cancelacion.DevolucionTotalAgencia;
import destinosonado.Pagos.Cancelacion.PoliticaDevolucion;
import destinosonado.Pagos.PagoEfectivo;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.controlador.ControladorReservas;
import destinosonado.controlador.RepositorioEnMemoria;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.modelo.enums.EstadoReserva;
import destinosonado.modelo.enums.TipoPasajero;
import destinosonado.servicios.CalculadoraPrecio;

/* Politicas de devolucion y cancelacion de reservas. */
public class DevolucionTest {

    private static final LocalDate SALIDA = LocalDate.of(2027, 3, 1);

    private static double porcentaje(PoliticaDevolucion p, int diasAntes) {
        return p.porcentajeDevolucion(SALIDA.minusDays(diasAntes), SALIDA);
    }

    private static List<DatosPasajero> adultos(int n) {
        List<DatosPasajero> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lista.add(new DatosPasajero("P" + i, "Prueba", "3000000" + i, "11-1111-1111",
                    LocalDate.of(1990, 1, 1), TipoPasajero.ADULTO, "11-5555-0000"));
        }
        return lista;
    }

    public static void main(String[] args) throws Exception {
        PoliticaDevolucion normal = new DevolucionPorAnticipacion();
        Verificador.igual("30 dias antes devuelve el 90 %", 0.90, porcentaje(normal, 30));
        Verificador.igual("60 dias antes devuelve el 90 %", 0.90, porcentaje(normal, 60));
        Verificador.igual("29 dias antes devuelve el 50 %", 0.50, porcentaje(normal, 29));
        Verificador.igual("15 dias antes devuelve el 50 %", 0.50, porcentaje(normal, 15));
        Verificador.igual("14 dias antes devuelve el 20 %", 0.20, porcentaje(normal, 14));
        Verificador.igual("7 dias antes devuelve el 20 %", 0.20, porcentaje(normal, 7));
        Verificador.igual("6 dias antes no devuelve nada", 0.0, porcentaje(normal, 6));
        Verificador.igual("si la causa es la agencia se devuelve el 100 %", 1.0,
                porcentaje(new DevolucionTotalAgencia(), 2));

        // Cancelacion de una reserva con 2 pasajeros (total 2000) y 1000 pagados
        RepositorioEnMemoria<Salida> repoSalidas = new RepositorioEnMemoria<>(Salida::getId);
        RepositorioEnMemoria<Reserva> repoReservas = new RepositorioEnMemoria<>(Reserva::getId);
        Salida salida = new Salida(1, new TourDePrueba(2, 10, 1000, false), LocalDate.now().plusDays(40), null, null);
        repoSalidas.guardar(salida);
        ControladorReservas controlador = new ControladorReservas(repoSalidas, repoReservas, new CalculadoraPrecio());

        Reserva r1 = controlador.crearReserva(1, null, null, adultos(2));
        controlador.registrarPago(r1.getId(), new PagoEfectivo(new Date(), 1000));
        Verificador.igualTexto("al pagar mas del 30 % la reserva queda CONFIRMADA", EstadoReserva.CONFIRMADA, r1.getEstado());

        Cancelacion c1 = controlador.cancelarReserva(r1.getId(), "El cliente no puede viajar", false);
        Verificador.igual("cancelando con 40 dias se devuelve el 90 % de lo pagado", 900, c1.getImporteDevuelto());
        Verificador.igualTexto("la reserva cancelada queda ANULADA, no se elimina", EstadoReserva.ANULADA, r1.getEstado());
        Verificador.igual("al cancelar se liberan los 2 lugares", 10, salida.lugaresDisponibles());
        Verificador.igual("la reserva anulada sigue en el repositorio", 1, repoReservas.listarTodos().size());

        Verificador.lanza("no se puede cancelar dos veces la misma reserva",
                ReglaDeNegocioException.class, () -> controlador.cancelarReserva(r1.getId(), "otra vez", false));
        Verificador.lanza("no se puede pagar una reserva anulada",
                ReglaDeNegocioException.class, () -> controlador.registrarPago(r1.getId(), new PagoEfectivo(new Date(), 10)));
        Verificador.lanza("cancelar una reserva inexistente lanza ReglaDeNegocio",
                ReglaDeNegocioException.class, () -> controlador.cancelarReserva(99, "no existe", false));

        Reserva r2 = controlador.crearReserva(1, null, null, adultos(1));
        controlador.registrarPago(r2.getId(), new PagoEfectivo(new Date(), 1000));
        Cancelacion c2 = controlador.cancelarReserva(r2.getId(), "Se cancela la salida", true, LocalDate.now().plusDays(35));
        Verificador.igual("por causa de la agencia se devuelve el 100 % aunque falte poco", 1000, c2.getImporteDevuelto());

        Reserva r3 = controlador.crearReserva(1, null, null, adultos(1));
        controlador.registrarPago(r3.getId(), new PagoEfectivo(new Date(), 1000));
        Cancelacion c3 = controlador.cancelarReserva(r3.getId(), "Cancela a ultimo momento", false, salida.getFechaInicio().minusDays(3));
        Verificador.igual("cancelando con 3 dias de anticipacion no se devuelve nada", 0, c3.getImporteDevuelto());

        Verificador.resumen();
    }
}
