package destinosonado.excepciones;

//Se utiliza cuando se piden mas lugares de los que quedan en una salida.

public class CupoInsuficienteException extends ReglaDeNegocioException {

    public CupoInsuficienteException(int solicitados, int disponibles) {
        super("Cupo insuficiente: se pidieron " + solicitados
                + " lugares y quedan " + disponibles);
    }
}