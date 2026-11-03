package destinosonado.Empleados;

public class Administrador extends Empleado {

    //Constructor
    public Administrador(String nombre, String apellido, String documento, String telefono, int legajo) {
        super(nombre, apellido, documento, telefono, legajo);
    }

    //Rol
    @Override
    public String descripcionRol() {
        return "Administrador";
    }
}
