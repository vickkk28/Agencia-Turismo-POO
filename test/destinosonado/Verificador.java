package destinosonado;

 //Ayuda minima para escribir pruebas unitarias sin librerias externas.
 //Cada metodo imprime el resultado de la comprobacion y lleva la cuenta.
 
public final class Verificador {

    //Bloque de codigo que se espera que lance una excepcion.
    public interface Accion {
        void ejecutar() throws Exception;
    }

    private static int aciertos = 0;
    private static int fallos = 0;

    private Verificador() {
    }

    public static void igual(String descripcion, double esperado, double obtenido) {
        registrar(descripcion, Math.abs(esperado - obtenido) < 0.0001, esperado, obtenido);
    }

    public static void igualTexto(String descripcion, Object esperado, Object obtenido) {
        registrar(descripcion, esperado.equals(obtenido), esperado, obtenido);
    }

    public static void verdadero(String descripcion, boolean condicion) {
        registrar(descripcion, condicion, true, condicion);
    }

    //Comprueba que la accion lance una excepcion del tipo indicado.
    public static void lanza(String descripcion, Class<? extends Exception> tipoEsperado, Accion accion) {
        String obtenido = "no lanzo nada";
        boolean ok = false;
        try {
            accion.ejecutar();
        } catch (Exception e) {
            obtenido = e.getClass().getSimpleName();
            ok = tipoEsperado.isInstance(e);
        }
        registrar(descripcion, ok, tipoEsperado.getSimpleName(), obtenido);
    }

    private static void registrar(String descripcion, boolean ok, Object esperado, Object obtenido) {
        if (ok) {
            aciertos++;
        } else {
            fallos++;
        }
        System.out.println((ok ? "OK    " : "FALLA ") + descripcion
                + " | esperado: " + esperado + " | obtenido: " + obtenido);
    }

    public static void resumen() {
        System.out.println("---- " + aciertos + " correctas, " + fallos + " fallidas ----");
    }
}