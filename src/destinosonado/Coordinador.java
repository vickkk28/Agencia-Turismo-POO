package destinosonado;

public class Coordinador extends Empleado{
    public Coordinador(String nombre, String apellido, String documento, String telefono, int legajo) {
        super(nombre, apellido, documento, telefono, legajo);
    }

    @Override
    public String descripcionRol() {
        return "Coordinador de operaciones"; // o "Coordinador" según el diagrama
    }
}
