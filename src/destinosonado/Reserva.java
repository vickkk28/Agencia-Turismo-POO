package destinosonado;
import destinosonado.Empleados.Vendedor;
import destinosonado.Pagos.Cancelacion.Cancelacion;
import destinosonado.Pagos.Cancelacion.NotaAjuste;
import destinosonado.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int numero;
    private LocalDate fecha;
    private EstadoReserva estado;
    private double total;
    private List<Pasajero> pasajeros;
    private Cliente titular;
    private Vendedor vendedor;
    private List<NotaAjuste> notasAjuste;
    private Cancelacion cancelacion;
    private Salida salida;

    public Reserva(int numero, LocalDate fecha,EstadoReserva estado ,Double total , Cliente titular, Vendedor vendedor ,Salida salida) {
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total ;
        this.titular = titular;
        this.vendedor = vendedor;
        this.salida=salida;
        this.pasajeros = new ArrayList<>();
        this.notasAjuste = new ArrayList<>();
        this.cancelacion = null;


    }

    public Pasajero agregarPasajero(Pasajero pasajero) {
        this.pasajeros.add(pasajero);
        return pasajero;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public int cantidadPasajeros() {
        int contador = 0;
        for (Pasajero pasajero : this.pasajeros) {
            contador++;
        }
        return contador;
    }

    public LocalDate fechaSalida() {
        if (this.fecha != null) {
            return this.salida.getFechaInicio();
        }
        return null;
    }

    public double saldo() {
        return this.total - this.saldo();
    }

    public boolean puedeCancelarse() {
        return this.cancelacion == null;
    }

    public Cancelacion cancelar(String motivo, double porcentaje) {
        double importeDevuelto = this.total * (porcentaje / 100.0);
        this.cancelacion = new Cancelacion(LocalDate.now(), motivo, importeDevuelto);
        return this.cancelacion;
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

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public Cliente getTitular() {
        return titular;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public Salida getSalida() {
        return salida;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public Cancelacion getCancelacion() {
        return cancelacion;
    }

    public List<NotaAjuste> getNotasAjuste() {
        return notasAjuste;
    }










}
