package destinosonado;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.modelo.enums.EstadoReserva;
import destinosonado.Empleados.Vendedor;
import destinosonado.Pagos.Cancelacion.Cancelacion;
import destinosonado.Pagos.Pago;

/**
 * Reserva de un cliente para una salida, con uno o mas pasajeros.
 * Conoce sus pagos y su saldo (Experto) y crea sus pasajeros y su cancelacion (Creador).
 * El calculo del precio y de la devolucion NO es responsabilidad de esta clase.
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final double PORCENTAJE_SENIA = 0.30;
    private static final double TOLERANCIA = 0.001;

    private final int numero;
    private final LocalDate fecha;
    private final Cliente cliente;
    private final Vendedor vendedor;
    private final Salida salida;
    private final List<Pasajero> pasajeros = new ArrayList<>();
    private final List<Pago> pagos = new ArrayList<>();
    private EstadoReserva estado;
    private double total;
    private Cancelacion cancelacion;

    public Reserva(int numero, LocalDate fecha, Cliente cliente, Vendedor vendedor, Salida salida) {
        this.numero = numero;
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.salida = salida;
        this.estado = EstadoReserva.PENDIENTE;
    }

    /**
     * Crea el pasajero a partir de los datos, le carga los servicios que contrato,
     * lo valida contra el tour (edad minima, seguro obligatorio) y lo agrega.
     */
    public Pasajero agregarPasajero(DatosPasajero datos) throws ReglaDeNegocioException {
        Pasajero pasajero = new Pasajero(datos.getNombre(), datos.getApellido(),
                datos.getDocumento(), datos.getTelefono(), datos.getFechaNacimiento(),
                datos.getTipo(), datos.getContactoEmergencia());
        for (ServicioAdicional servicio : datos.getServicios()) {
            pasajero.contratarServicio(servicio);
        }
        salida.validarPasajero(pasajero);
        pasajeros.add(pasajero);
        return pasajero;
    }

    /** Si el tour exige seguro de viaje, todos los pasajeros deben tenerlo contratado. */
    public void validarServiciosObligatorios() throws ReglaDeNegocioException {
        if (!salida.requiereSeguro()) {
            return;
        }
        for (Pasajero p : pasajeros) {
            if (!p.tieneSeguro()) {
                throw new ReglaDeNegocioException("El tour requiere seguro de viaje y falta contratarlo para "
                        + p.getNombreCompleto());
            }
        }
    }

    public int cantidadPasajeros() {
        return pasajeros.size();
    }

    public LocalDate fechaSalida() {
        return salida.getFechaInicio();
    }

    public double totalServiciosAdicionales() {
        double suma = 0;
        for (Pasajero p : pasajeros) {
            for (ServicioAdicional s : p.getServicios()) {
                suma += s.getPrecio();
            }
        }
        return suma;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        this.total = total;
    }

    /** Registra un pago. Con la senia (30 %) la reserva se confirma. */
    public void registrarPago(Pago pago) throws ReglaDeNegocioException {
        if (estado == EstadoReserva.ANULADA || estado == EstadoReserva.FINALIZADA) {
            throw new ReglaDeNegocioException("La reserva " + numero + " esta " + estado
                    + " y no admite pagos");
        }
        if (pago.getImporte() > saldo() + TOLERANCIA) {
            throw new ReglaDeNegocioException("El pago supera el saldo pendiente de la reserva " + numero);
        }
        pagos.add(pago);
        confirmarSiCorresponde();
    }

    public double montoPagado() {
        double suma = 0;
        for (Pago p : pagos) {
            suma += p.getImporte();
        }
        return suma;
    }

    public double saldo() {
        return total - montoPagado();
    }

    public void confirmarSiCorresponde() {
        if (estado == EstadoReserva.PENDIENTE && montoPagado() >= total * PORCENTAJE_SENIA - TOLERANCIA) {
            estado = EstadoReserva.CONFIRMADA;
            salida.confirmarSiCorresponde();
        }
    }

    public boolean puedeCancelarse() {
        return estado == EstadoReserva.PENDIENTE || estado == EstadoReserva.CONFIRMADA;
    }

    /**
     * Cancela la reserva. El porcentaje de devolucion (entre 0 y 1) lo decide
     * una PoliticaDevolucion; la reserva solo lo aplica sobre lo pagado.
     * La reserva no se elimina: queda ANULADA con su cancelacion registrada.
     */
    public Cancelacion cancelar(String motivo, double porcentajeDevolucion) throws ReglaDeNegocioException {
        if (!puedeCancelarse()) {
            throw new ReglaDeNegocioException("La reserva " + numero + " no puede cancelarse (estado "
                    + estado + ")");
        }
        if (porcentajeDevolucion < 0 || porcentajeDevolucion > 1) {
            throw new ReglaDeNegocioException("El porcentaje de devolucion debe estar entre 0 y 1");
        }
        double importeDevuelto = montoPagado() * porcentajeDevolucion;
        cancelacion = new Cancelacion(LocalDate.now(), motivo, importeDevuelto);
        estado = EstadoReserva.ANULADA;
        salida.liberar(cantidadPasajeros());
        return cancelacion;
    }

    public int getId() {
        return numero;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public Salida getSalida() {
        return salida;
    }

    public Cancelacion getCancelacion() {
        return cancelacion;
    }

    public List<Pasajero> getPasajeros() {
        return Collections.unmodifiableList(pasajeros);
    }

    public List<Pago> getPagos() {
        return Collections.unmodifiableList(pagos);
    }
}
