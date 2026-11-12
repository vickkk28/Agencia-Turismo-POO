package destinosonado.Persistencia;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Archivo unico donde se guardan todas las colecciones del sistema (tours, salidas, reservas).
 * Se escribe todo junto en una sola operacion para que los objetos que se
 * referencian entre si (una Reserva y su Salida, una Salida y su Tour) se
 * lean despues como los mismos objetos y no como copias distintas.
 */
public class AlmacenArchivo {

    private final String ruta;
    private HashMap<String, LinkedHashMap<Integer, Object>> colecciones = new HashMap<>();

    /** Abre el almacen; si el archivo todavia no existe (primera ejecucion) arranca vacio. */
    public AlmacenArchivo(String ruta) {
        this.ruta = ruta;
        cargar();
    }

    /** Devuelve la coleccion con ese nombre (id -> objeto), creandola si no existe. */
    public Map<Integer, Object> coleccion(String nombre) {
        return colecciones.computeIfAbsent(nombre, n -> new LinkedHashMap<>());
    }

    @SuppressWarnings("unchecked")
    private void cargar() {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return; // primera ejecucion: no hay datos guardados todavia
        }
        ObjectInputStream entrada = null;
        try {
            entrada = new ObjectInputStream(new BufferedInputStream(new FileInputStream(archivo)));
            colecciones = (HashMap<String, LinkedHashMap<Integer, Object>>) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("No se pudo leer el archivo de datos " + ruta, e);
        } finally {
            cerrar(entrada);
        }
    }

    /** Escribe todas las colecciones en el archivo. */
    public void guardar() {
        File archivo = new File(ruta);
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }
        ObjectOutputStream salida = null;
        try {
            salida = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(archivo)));
            salida.writeObject(colecciones);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de datos " + ruta, e);
        } finally {
            cerrar(salida); // se cierra siempre, haya salido bien o mal
        }
    }

    private void cerrar(java.io.Closeable flujo) {
        if (flujo != null) {
            try {
                flujo.close();
            } catch (IOException e) {
                System.out.println("No se pudo cerrar el archivo " + ruta);
            }
        }
    }
}
