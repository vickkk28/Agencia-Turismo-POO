package destinosonado.Vista;

import destinosonado.Empleados.Vendedor;
import destinosonado.controlador.ControladorReservas;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FormularioModificar extends JFrame {

    private final ControladorReservas controlador;
    private final Vendedor vendedor;
    private final Object idReserva;
    private final Consumer<String> alModificar;

    private final JComboBox<String> cbEstado =
            new JComboBox<>(new String[]{"Pendiente", "Confirmada", "Cancelada"});
    private final JButton btnGuardar = new JButton("Guardar cambios");
    private final JButton btnCancelar = new JButton("Cancelar");

    public FormularioModificar(ControladorReservas controlador, Vendedor vendedor,
                               Object idReserva, String estadoActual, Consumer<String> alModificar) {
        this.controlador = controlador;
        this.vendedor = vendedor;
        this.idReserva = idReserva;
        this.alModificar = alModificar;

        setTitle("Modificar Reserva - ID: " + idReserva);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        cbEstado.setSelectedItem(estadoActual);
        armarVentana();

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void armarVentana() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Actualizar Datos de Reserva"));
        panel.add(new JLabel("ID de Reserva:"));
        panel.add(new JLabel(String.valueOf(idReserva)));
        panel.add(new JLabel("Estado:"));
        panel.add(cbEstado);

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        setLayout(new BorderLayout(5, 5));
        add(panel, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void guardarCambios() {
        try {
            String nuevoEstado = (String) cbEstado.getSelectedItem();

            if (alModificar != null) {
                alModificar.accept(nuevoEstado); // avisa a la ventana principal
            }
            JOptionPane.showMessageDialog(this, "Reserva modificada con éxito.",
                    "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}