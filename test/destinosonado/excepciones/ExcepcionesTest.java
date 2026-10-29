package destinosonado.excepciones;

import destinosonado.Verificador;

public class ExcepcionesTest {

    public static void main(String[] args) {
        CupoInsuficienteException e = new CupoInsuficienteException(5, 2);

        Verificador.verdadero("CupoInsuficiente es una ReglaDeNegocio",
                e instanceof ReglaDeNegocioException);
        Verificador.verdadero("el mensaje indica los lugares pedidos",
                e.getMessage().contains("5"));
        Verificador.verdadero("el mensaje indica los lugares disponibles",
                e.getMessage().contains("2"));
        Verificador.resumen();
    }
}