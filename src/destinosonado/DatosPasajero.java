package destinosonado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import destinosonado.modelo.enums.TipoPasajero;

//Datos de un pasajero tal como los ingresa el vendedor, con ellos la Reserva crea el Pasajero. Puede incluir los servicios adicionales que contrata ese pasajero.
public class DatosPasajero {

    private final String nombre;
    private final String apellido;
    private final String documento;
    private final String telefono;
    private final LocalDate fechaNacimiento;
    private final TipoPasajero tipo;
    private final String contactoEmergencia;
    private final List<ServicioAdicional> servicios;

    public DatosPasajero(String nombre, String apellido, String documento, String telefono,
                         LocalDate fechaNacimiento,
                         TipoPasajero tipo, String contactoEmergencia) {
        this(nombre, apellido, documento, telefono, fechaNacimiento, tipo, contactoEmergencia, new ArrayList<>());
    }

    public DatosPasajero(String nombre, String apellido, String documento, String telefono,
                         LocalDate fechaNacimiento,
                         TipoPasajero tipo, String contactoEmergencia,
                         List<ServicioAdicional> servicios) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
        this.contactoEmergencia = contactoEmergencia;
        this.servicios = new ArrayList<>(servicios);
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public TipoPasajero getTipo() {
        return tipo;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public List<ServicioAdicional> getServicios() {
        return Collections.unmodifiableList(servicios);
    }
}
