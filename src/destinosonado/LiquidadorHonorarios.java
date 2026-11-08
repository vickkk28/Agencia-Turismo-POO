package destinosonado;

import destinosonado.model.Guia;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Salida;

public class LiquidadorHonorarios {
    public LiquidadorHonorarios() {
    }

    public Liquidacion liquidar(Salida salida) throws ReglaDeNegocioException {
        if (salida == null) {
            throw new ReglaDeNegocioException("No se puede liquidar honorarios de una salida nula.");
        }

        Guia guia = salida.getGuia();
        if (guia == null) {
            throw new ReglaDeNegocioException("La salida no tiene un guía asignado para liquidar.");
        }

        int diasTrabajados = salida.getTour().getDuracionDias();

        // Monto total = días trabajados * honorario por día del guía
        double monto = diasTrabajados * guia.getHonorarioPorDia();

        // Retorna el objeto Liquidacion con los datos calculados
        return new Liquidacion(java.time.LocalDate.now(), diasTrabajados, monto);
    }

}
