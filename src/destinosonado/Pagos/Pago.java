package destinosonado.Pagos;

import java.util.Date;

public abstract class Pago implements java.io.Serializable {
    private Date fecha;
    private double importe;

    //Constructor
    public Pago(Date fecha, double importe) {
        this.fecha = fecha;
        this.importe = importe;
    }

    //Métodos
    public abstract double calcularRecargo();

    public double calcularTotal() {
        return importe + calcularRecargo();
    }

    //Getters
    public Date getFecha() {
        return fecha;
    }

    //Setters
    public double getImporte() {
        return importe;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}