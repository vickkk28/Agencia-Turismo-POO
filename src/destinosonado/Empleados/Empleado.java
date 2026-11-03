package destinosonado.Empleados;

import destinosonado.Persona;

public class Empleado extends Persona {
    private int legajo;

    //Constructor
    public Empleado(String nombre, String apellido, String documento, String telefono , int legajo) {
        super(nombre, apellido, documento, telefono);
        this.legajo = legajo;
    }

    //Rol
    public String descripcionRol() {
        return "Empleado";
    }
}
