package destinosonado;

//Cada metodo imprime el resultado de la comprobacion y lleva la cuenta.
 
public final class Verificador {

    private static int aciertos = 0;
    private static int fallos = 0;

    private Verificador() {
    }

    public static void igual(String descripcion, double esperado, double obtenido) {
        registrar(descripcion, Math.abs(esperado - obtenido) < 0.0001, esperado, obtenido);
    }

    public static void verdadero(String descripcion, boolean condicion) {
        registrar(descripcion, condicion, true, condicion);
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