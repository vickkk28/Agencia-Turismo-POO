package destinosonado.Pagos;

import java.util.Date;

public class PagoTarjeta extends Pago {
    private int cuotas;

    //Constructor
    public PagoTarjeta(Date fecha, double importe, int cuotas) {
        super(fecha, importe);
        this.cuotas = cuotas;
    }

    // Recargo completo
    @Override
    public double calcularRecargo() {
        if (cuotas > 1) {
            return getImporte() * 0.07;
        }
        return 0;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }
}