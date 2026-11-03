package destinosonado.Pagos;

import java.util.Date;

public class PagoEfectivo extends Pago {

    //Constructor
    public PagoEfectivo(Date fecha, double importe) {
        super(fecha, importe);
    }

    //Recargo 0
    @Override
    public double calcularRecargo() {
        return 0;
    }
}