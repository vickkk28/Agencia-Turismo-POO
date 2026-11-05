package destinosonado;

import destinosonado.modelo.enums.TipoPasajero;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pasajero extends Persona {
    private LocalDate fechaNacimiento;
    private TipoPasajero tipo;
    private String contactoEmergencia;
    private String restricciones;
    private List<ServicioAdicional> serviciosContratados = new ArrayList<>();

    //Constructores
    // Constructor completo: lo usa Reserva.agregarPasajero
    public Pasajero(String nombre, String apellido, String documento, String telefono,
                    LocalDate fechaNacimiento, TipoPasajero tipo, String contactoEmergencia) {
        super(nombre, apellido, documento, telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
        this.contactoEmergencia = contactoEmergencia;
    }

    // Constructor original (corregido: ahora guarda restricciones y servicios)
    public Pasajero(String nombre, String apellido, String documento, String telefono ,
                    LocalDate fechaNacimiento , String restricciones,
                    List<ServicioAdicional> serviciosContratados  ) {
        super(nombre, apellido, documento, telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.restricciones = restricciones;
        if (serviciosContratados != null) {
            this.serviciosContratados = new ArrayList<>(serviciosContratados);
        }
    }

    //Métodos
    public int CalcularEdad(LocalDate fecha) {
        if (this.fechaNacimiento == null) {
            return 0;
        }
        return fecha.getYear() - this.fechaNacimiento.getYear();
    }

    public boolean tieneSeguro() {
        for (ServicioAdicional servicio : this.serviciosContratados) {
            if (servicio.esSeguro()) {
                return true;
            }
        }
        return false;
    }

    public void contratarServicio(ServicioAdicional servicio) {
        this.serviciosContratados.add(servicio);
    }

    //Getters
    public List<ServicioAdicional> getServicios() {
        return Collections.unmodifiableList(serviciosContratados);
    }

    public String getRestricciones() {
        return restricciones;
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

    //Setters
    public void setTipo(TipoPasajero tipo) {
        this.tipo = tipo;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
                this.contactoEmergencia = contactoEmergencia;
    }
}
