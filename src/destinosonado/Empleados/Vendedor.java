package destinosonado.Empleados;

public class Vendedor extends Empleado {
    private double comision;

    //Constructor
    public Vendedor(String nombre, String apellido, String documento, String telefono, int legajo, double comision) {
        super(nombre, apellido, documento, telefono, legajo);
        this.comision = comision;
    }

    //Rol
    public String descripcionRol() {
        return "Vendedor";
    }

    //Obtenciones
    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

}