package destinosonado.Empleados;

import destinosonado.Empleado;

public class Vendedor extends Empleado {
    private double comision;

    public Vendedor(String nombre, String apellido, String documento, String telefono, int legajo, double comision) {
        super(nombre, apellido, documento, telefono, legajo);
        this.comision = comision;
    }

    public String descripcionRol() {
        return "Vendedor";
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

}