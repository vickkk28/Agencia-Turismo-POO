package destinosonado;

import destinosonado.Empleados.Guia;
import destinosonado.modelo.enums.Categoria;

import java.time.LocalDate;
import java.util.List;

public class Tour {
    private String codigo;
    private String nombre;
    private String descripcion;
    private int duracionDias;
    private int cupoMinimo;
    private int cupoMaximo;
    private List<Salida> salidas;
    private List<Actividad> actividades;
    private List<Tarifa> tarifas;
    private Categoria categoria;

    public Tour(String codigo , String nombre, String descripcion, int duracionDias, int cupoMinimo, int cupoMaximo, List<Salida> salidas, List<Actividad> actividades, List<Tarifa> tarifas , Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionDias = duracionDias;
        this.cupoMinimo = cupoMinimo;
        this.cupoMaximo = cupoMaximo;
        this.salidas = salidas;
        this.actividades = actividades;
        this.tarifas = tarifas;
        this.categoria=categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public boolean requiereSeguro() {
        return categoria == Categoria.AVENTURA;
    }

    public void validarPasajero(Pasajero p) {

        if (p == null) {
            throw new IllegalArgumentException("El pasajero no puede ser nulo");
        }

        if (requiereSeguro() && !p.tieneSeguro()) {
            throw new IllegalArgumentException(
                    "El pasajero debe contar con seguro para realizar un tour de aventura"
            );
        }
    }

    public double precioBaseFecha(LocalDate fecha) {
        if (fecha == null || tarifas == null) {
            return 0.0;
        }

        for (Tarifa tarifa : tarifas) {
            boolean desdeOk = fecha.compareTo(tarifa.getVigenciaDesde()) >= 0;
            boolean hastaOk = fecha.compareTo(tarifa.getVigenciaHasta()) <= 0;

            if (desdeOk && hastaOk) {
                return tarifa.getPrecioBase();
            }
        }

        throw new IllegalArgumentException("No hay una tarifa vigente para la fecha: " + fecha);
    }

    // El id lo asigna quien programa la salida (el controlador), igual que el numero de reserva
    public Salida programarSalida(int id, LocalDate inicio, Guia guia, Vehiculo vehiculo) {
        Salida nuevaSalida = new Salida(id, this, inicio, guia, vehiculo);

        // Se añade a la colección de la agregación
        this.salidas.add(nuevaSalida);

        return nuevaSalida;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionDias() {
        return duracionDias;
    }
    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public int getCupoMinimo() {
        return cupoMinimo;
    }
    public void setCupoMinimo(int cupoMinimo) {
        this.cupoMinimo = cupoMinimo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }
    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public List<Salida> getSalidas() {
        return salidas;
    }
    public void setSalidas(List<Salida> salidas) {
        this.salidas = salidas;
    }

    public List<Actividad> getActividades() {
        return actividades; }
    public void setItinerario(List<Actividad> itinerario) { this.actividades = itinerario; }

    public List<Tarifa> getTarifas() { return tarifas; }
    public void setTarifas(List<Tarifa> tarifas) { this.tarifas = tarifas; }


}
