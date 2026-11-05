package destinosonado.controlador;

import java.time.LocalDate;
import java.util.List;

import destinosonado.Cliente;
import destinosonado.DatosPasajero;
import destinosonado.Empleados.Vendedor;
import destinosonado.Reserva;
import destinosonado.Salida;
import destinosonado.excepciones.CupoInsuficienteException;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.persistencia.Repositorio;
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

    public ControladorReservas(Repositorio<Salida> repoSalidas, Repositorio<Reserva> repoReservas,
                               CalculadoraPrecio calculadora) {
        this.repoSalidas = repoSalidas;
        this.repoReservas = repoReservas;
        this.calculadora = calculadora;
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

    private int proximoNumero() {
        return repoReservas.listarTodos().size() + 1;
    }
}