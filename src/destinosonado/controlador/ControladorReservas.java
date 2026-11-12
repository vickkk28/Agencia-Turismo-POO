package destinosonado.controlador;

import java.time.LocalDate;
import java.util.List;

import destinosonado.excepciones.CupoInsuficienteException;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.Cliente;
import destinosonado.DatosPasajero;
import destinosonado.Empleados.Vendedor;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.Pagos.Cancelacion.Cancelacion;
import destinosonado.Pagos.Cancelacion.DevolucionPorAnticipacion;
import destinosonado.Pagos.Cancelacion.DevolucionTotalAgencia;
import destinosonado.Pagos.Cancelacion.PoliticaDevolucion;
import destinosonado.Pagos.Pago;
import destinosonado.Persistencia.Repositorio;
import destinosonado.servicios.CalculadoraPrecio;

/**
 * Controlador de los casos de uso de reservas (patron Controlador).
 * Recibe las operaciones de la interfaz y las coordina; depende de la
 * abstraccion Repositorio y no de como se guardan los datos (DIP).
 */
public class ControladorReservas {

    private final Repositorio<Salida> repoSalidas;
    private final Repositorio<Reserva> repoReservas;
    private final CalculadoraPrecio calculadora;
    private final PoliticaDevolucion politicaNormal;
    private final PoliticaDevolucion politicaAgencia;

    public ControladorReservas(Repositorio<Salida> repoSalidas, Repositorio<Reserva> repoReservas,
                               CalculadoraPrecio calculadora) {
        this(repoSalidas, repoReservas, calculadora,
                new DevolucionPorAnticipacion(), new DevolucionTotalAgencia());
    }

    /** Permite elegir las politicas de devolucion (estrategias) sin tocar el controlador. */
    public ControladorReservas(Repositorio<Salida> repoSalidas, Repositorio<Reserva> repoReservas,
                               CalculadoraPrecio calculadora, PoliticaDevolucion politicaNormal,
                               PoliticaDevolucion politicaAgencia) {
        this.repoSalidas = repoSalidas;
        this.repoReservas = repoReservas;
        this.calculadora = calculadora;
        this.politicaNormal = politicaNormal;
        this.politicaAgencia = politicaAgencia;
    }

    /** CU04 - Gestionar reservas (incluye CU05 - Calcular precio). */
    public Reserva crearReserva(int idSalida, Cliente cliente, Vendedor vendedor,
                                List<DatosPasajero> datosPasajeros) throws ReglaDeNegocioException {
        Salida salida = repoSalidas.buscarPorId(idSalida);
        if (salida == null) {
            throw new ReglaDeNegocioException("No existe la salida " + idSalida);
        }
        if (!salida.admiteReservas()) {
            throw new ReglaDeNegocioException("La salida " + idSalida + " no admite reservas (estado "
                    + salida.getEstado() + ")");
        }
        int pedidos = datosPasajeros.size();
        if (pedidos == 0) {
            throw new ReglaDeNegocioException("La reserva debe tener al menos un pasajero");
        }
        if (salida.lugaresDisponibles() < pedidos) {
            throw new CupoInsuficienteException(pedidos, salida.lugaresDisponibles());
        }

        Reserva reserva = new Reserva(proximoNumero(), LocalDate.now(), cliente, vendedor, salida);
        for (DatosPasajero datos : datosPasajeros) {
            reserva.agregarPasajero(datos);
        }
        reserva.validarServiciosObligatorios();

        reserva.setTotal(calculadora.calcularTotal(reserva));
        salida.ocupar(pedidos);
        salida.agregarReserva(reserva);
        repoSalidas.guardar(salida);
        repoReservas.guardar(reserva);
        return reserva;
    }

    /** CU - Registrar pago. Con la senia (30 %) la reserva pasa a CONFIRMADA. */
    public Reserva registrarPago(int idReserva, Pago pago) throws ReglaDeNegocioException {
        Reserva reserva = buscarReserva(idReserva);
        reserva.registrarPago(pago);
        repoReservas.guardar(reserva);
        repoSalidas.guardar(reserva.getSalida());
        return reserva;
    }

    /** CU - Cancelar reserva con la politica de devolucion que corresponda. */
    public Cancelacion cancelarReserva(int idReserva, String motivo, boolean culpaAgencia)
            throws ReglaDeNegocioException {
        return cancelarReserva(idReserva, motivo, culpaAgencia, LocalDate.now());
    }

    /** Igual que la anterior pero con la fecha de cancelacion explicita (util para pruebas). */
    public Cancelacion cancelarReserva(int idReserva, String motivo, boolean culpaAgencia, LocalDate hoy)
            throws ReglaDeNegocioException {
        Reserva reserva = buscarReserva(idReserva);
        PoliticaDevolucion politica = culpaAgencia ? politicaAgencia : politicaNormal;
        double porcentaje = politica.porcentajeDevolucion(hoy, reserva.fechaSalida());
        Cancelacion cancelacion = reserva.cancelar(motivo, porcentaje);
        repoReservas.guardar(reserva);
        repoSalidas.guardar(reserva.getSalida());
        return cancelacion;
    }

    private Reserva buscarReserva(int idReserva) throws ReglaDeNegocioException {
        Reserva reserva = repoReservas.buscarPorId(idReserva);
        if (reserva == null) {
            throw new ReglaDeNegocioException("No existe la reserva " + idReserva);
        }
        return reserva;
    }

    private int proximoNumero() {
        return repoReservas.listarTodos().size() + 1;
    }
}
