package destinosonado.Pagos;

import java.util.Date;

public class PagoTransferencia extends Pago {

    //Constructor
    public PagoTransferencia(Date fecha, double importe) {
        super(fecha, importe);
    }

    //Recargo 0
    @Override
    public double calcularRecargo() {

        return 0;
    }
}