package destinosonado.excepciones;

//Se utiliza cuando una operacion no cumple con una regla de la agencia.

public class ReglaDeNegocioException extends Exception {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}