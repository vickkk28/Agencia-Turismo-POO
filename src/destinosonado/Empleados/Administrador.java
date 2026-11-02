package destinosonado.Empleados;

import destinosonado.Empleado;

public class Administrador extends Empleado {
    public Administrador(String nombre, String apellido, String documento, String telefono, int legajo) {
        super(nombre, apellido, documento, telefono, legajo);
    }

    @Override
    public String descripcionRol() {
        return "Administrador";
    }
}
