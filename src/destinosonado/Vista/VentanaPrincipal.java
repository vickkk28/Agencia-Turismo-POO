package destinosonado.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal {
    private JTable TablaReservas;
    private JButton btnNuevo;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTextField textBuscar;
    private JPanel panelPrincipal;

    public VentanaPrincipal() {
        configurarTabla();

        // 1. Configuramos el filtro de búsqueda para la tabla
        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) TablaReservas.getModel());
        TablaReservas.setRowSorter(sorter);

        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hiciste clic en Nuevo");
            }
        });

        btnModificar.addActionListener(e -> {
            int filaSeleccionada = TablaReservas.getSelectedRow();
            if (filaSeleccionada != -1) {
                JOptionPane.showMessageDialog(null, "Modificar fila: " + filaSeleccionada);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, seleccioná una reserva de la tabla.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = TablaReservas.getSelectedRow();
            if (filaSeleccionada != -1) {
                DefaultTableModel modelo = (DefaultTableModel) TablaReservas.getModel();
                modelo.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(null, "Reserva eliminada con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, seleccioná una reserva para eliminar.");
            }
        });

        // 2. Lógica real del botón Buscar (filtra lo que escribas en el textBuscar)
        btnBuscar.addActionListener(e -> {
            String texto = textBuscar.getText().trim();
            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                // (?i) hace que no distinga entre mayúsculas y minúsculas
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        });
    }

    private void configurarTabla() {
        String[] columnas = {"ID Reserva", "Pasajero", "Destino / Tour", "Cupos", "Estado"};
        Object[][] datosPrueba = {
                {"001", "Juan Pérez", "Cataratas del Iguazú", "2", "Confirmada"},
                {"002", "María Gómez", "San Carlos de Bariloche", "4", "Pendiente"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datosPrueba, columnas);
        TablaReservas.setModel(modelo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Agencia de Turismo - Principal");
            VentanaPrincipal app = new VentanaPrincipal();
            frame.setContentPane(app.panelPrincipal);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(850, 550);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}