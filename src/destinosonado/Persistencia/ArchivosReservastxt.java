package destinosonado.Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Guarda y lee las filas de la tabla en un .txt (una reserva por línea, separada por ;). */
public class ArchivosReservastxt {

    private final File archivo;

    public ArchivosReservastxt(String nombreArchivo) {
        this.archivo = new File(nombreArchivo);
    }

    public List<String[]> leer() {
        List<String[]> filas = new ArrayList<>();
        if (!archivo.exists()) return filas; // primera vez: arranca vacío

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] campos = linea.split(";", -1);
                if (campos.length >= 5) filas.add(campos);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer " + archivo + ": " + e.getMessage());
        }
        return filas;
    }

    public void escribir(List<String[]> filas) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            for (String[] fila : filas) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < fila.length; i++) {
                    if (i > 0) sb.append(';');
                    sb.append(fila[i].replace(";", ",")); // el ; es el separador
                }
                pw.println(sb);
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar " + archivo + ": " + e.getMessage());
        }
    }
}