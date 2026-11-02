package destinosonado.Pagos;

import java.util.Date;

public class PagoEfectivo extends Pago {

    public PagoEfectivo(Date fecha, double importe) {
        super(fecha, importe);
    }

    @Override
    public double calcularRecargo() {
        return 0;
    }
}