package destinosonado.Pagos;

import java.util.Date;

public class PagoTarjeta extends Pago {
    private int cuotas;

    public PagoTarjeta(Date fecha, double importe, int cuotas) {
        super(fecha, importe);
        this.cuotas = cuotas;
    }

    // 10% de recargo solo si se paga en más de una cuota
    @Override
    public double calcularRecargo() {
        if (cuotas > 1) {
            return getImporte() * 0.10;
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