package destinosonado;

import destinosonado.modelo.enums.TipoPasajero;

import java.time.LocalDate;

public class Pasajero extends Persona {
    private LocalDate fechaNacimiento;
    private TipoPasajero tipo;
    private String contactoEmergencia;
    public Pasajero(String nombre, String apellido, String documento, String telefono) {
        super(nombre, apellido, documento, telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
        this.contactoEmergencia = contactoEmergencia;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoPasajero getTipo() {
        return tipo;
    }

    public void setTipo(TipoPasajero tipo) {
        this.tipo = tipo;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }
}
