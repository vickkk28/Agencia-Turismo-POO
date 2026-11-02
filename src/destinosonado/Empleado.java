package destinosonado;

public class Empleado extends Persona{
    private int legajo;

    public Empleado(String nombre, String apellido, String documento, String telefono , int legajo) {
        super(nombre, apellido, documento, telefono);
        this.legajo = legajo;
    }

    public Empleado() {
    }


    public String descripcionRol() {
        return "Empleado";
    }
}
