package destinosonado.Vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import destinosonado.servicios.ServicioReportes;

/** Pestaña de reportes: cada boton muestra un informe de ServicioReportes. */
public class PanelReportes extends JPanel {

    private final ServicioReportes servicio;
    private final JTextArea salida = new JTextArea();

    public PanelReportes(ServicioReportes servicio) {
        this.servicio = servicio;
        salida.setEditable(false);
        salida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        salida.setText("Elegi un reporte con los botones de arriba.");

        JPanel botones = new JPanel();
        botones.add(boton("Ocupacion por salida", "Ocupacion por salida", () -> servicio.ocupacionPorSalida()));
        botones.add(boton("Ventas por vendedor", "Ventas por vendedor", () -> servicio.ventasPorVendedor()));
        botones.add(boton("Ingresos por tour", "Ingresos por tour", () -> servicio.ingresosPorTour()));
        botones.add(boton("Cancelaciones", "Cancelaciones", () -> servicio.cancelaciones()));

        setLayout(new BorderLayout(5, 5));
        add(botones, BorderLayout.NORTH);
        add(new JScrollPane(salida), BorderLayout.CENTER);
    }

    private interface Reporte {
        List<String> generar();
    }

    private JButton boton(String texto, String titulo, Reporte reporte) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(titulo.toUpperCase() + "\n\n");
            List<String> lineas = reporte.generar();
            if (lineas.isEmpty()) {
                sb.append("(sin datos)");
            }
            for (String l : lineas) {
                sb.append(l).append('\n');
            }
            salida.setText(sb.toString());
            salida.setCaretPosition(0);
        });
        return b;
    }
}
