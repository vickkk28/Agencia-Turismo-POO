package destinosonado;

import destinosonado.modelo.enums.TipoPasajero;

import java.time.LocalDate;
import java.util.List;

public class Pasajero extends Persona {
    private LocalDate fechaNacimiento;
    private TipoPasajero tipo;
    private String contactoEmergencia;
    private String restricciones;
    private List<ServicioAdicional> serviciosContratados;

    //Constructor
    public Pasajero(String nombre, String apellido, String documento, String telefono ,
                    LocalDate fechaNacimiento , String restricciones,
                    List<ServicioAdicional> serviciosContratados  ) {
        super(nombre, apellido, documento, telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
        this.contactoEmergencia = contactoEmergencia;
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

    //Getters
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
