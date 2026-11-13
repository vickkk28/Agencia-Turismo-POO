package destinosonado.Vista;

import java.awt.Dimension;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;

/** Utilidades chicas que comparten las pestañas: formato de importes y carteles. */
final class FormatoUI {

    private FormatoUI() {
    }

    static String plata(double monto) {
        return "$" + String.format(Locale.US, "%,.0f", monto);
    }

    static void avisar(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "No se pudo continuar", JOptionPane.WARNING_MESSAGE);
    }

    static void informar(Component padre, String titulo, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Muestra un texto largo (listas, detalles) en un cuadro con barra de desplazamiento. */
    static void mostrarTexto(Component padre, String titulo, String texto) {
        JTextArea area = new JTextArea(texto, 14, 46);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(520, 260));
        JOptionPane.showMessageDialog(padre, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}
