package destinosonado.Pagos;

public class Pago {
    private int fecha;
    private int importe;
    public int recargo;

    //Constructor
    public Pago(int fecha, int importe, int recargo) {
        this.fecha = fecha;
        this.importe = importe;
        this.recargo = recargo;
    }

    //Getters
    public int getFecha() {
        return fecha;
    }
    public int getImporte() {
        return importe;
    }
    public int getRecargo() {
        return recargo;
    }

    //Setters
    public void setRecargo(int recargo) {
        this.recargo = recargo;
    }
    public void setImporte(int importe) {
        this.importe = importe;
    }
    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

}
