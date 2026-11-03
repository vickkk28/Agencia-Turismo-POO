package destinosonado.Pagos;

import java.util.Date;

public class PagoTransferencia extends Pago {

    public PagoTransferencia(Date fecha, double importe) {
        super(fecha, importe);
    }

    @Override
    public double calcularRecargo() {

        return 0;
    }
}