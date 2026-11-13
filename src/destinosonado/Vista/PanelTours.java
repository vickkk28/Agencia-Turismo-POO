package destinosonado.Vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import destinosonado.controlador.ControladorTours;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.Categoria;

/** Pestaña del catalogo de tours: lista los tours y permite dar de alta uno nuevo. */
public class PanelTours extends JPanel {

    private final ControladorTours controlador;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Codigo", "Nombre", "Categoria", "Dias", "Cupo", "Precio base hoy", "Seguro obligatorio"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public PanelTours(ControladorTours controlador) {
        this.controlador = controlador;
        JButton btnNuevo = new JButton("Nuevo tour");
        JButton btnActualizar = new JButton("Actualizar");
        btnNuevo.addActionListener(e -> nuevoTour());
        btnActualizar.addActionListener(e -> actualizar());

        JPanel botones = new JPanel();
        botones.add(btnNuevo);
        botones.add(btnActualizar);

        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        actualizar();
    }

    public void actualizar() {
        modelo.setRowCount(0);
        for (Tour t : controlador.listarTours()) {
            String precio;
            try {
                precio = FormatoUI.plata(t.precioBaseFecha(LocalDate.now()));
            } catch (IllegalArgumentException e) {
                precio = "sin tarifa vigente";
            }
            modelo.addRow(new Object[]{t.getCodigo(), t.getNombre(), t.getCategoria(), t.getDuracionDias(),
                    t.getCupoMinimo() + " a " + t.getCupoMaximo(), precio, t.requiereSeguro() ? "Si" : "No"});
        }
    }

    private void nuevoTour() {
        JTextField nombre = new JTextField();
        JTextField descripcion = new JTextField();
        JTextField dias = new JTextField("3");
        JTextField cupoMin = new JTextField("2");
        JTextField cupoMax = new JTextField("20");
        JComboBox<Categoria> categoria = new JComboBox<>(Categoria.values());
        JTextField precio = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        form.add(nombre);
        form.add(new JLabel("Descripcion:"));
        form.add(descripcion);
        form.add(new JLabel("Duracion (dias):"));
        form.add(dias);
        form.add(new JLabel("Cupo minimo:"));
        form.add(cupoMin);
        form.add(new JLabel("Cupo maximo:"));
        form.add(cupoMax);
        form.add(new JLabel("Categoria:"));
        form.add(categoria);
        form.add(new JLabel("Precio base por pasajero:"));
        form.add(precio);

        int opcion = JOptionPane.showConfirmDialog(this, form, "Nuevo tour",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Tour tour = controlador.crearTour(nombre.getText(), descripcion.getText(),
                    Integer.parseInt(dias.getText().trim()), Integer.parseInt(cupoMin.getText().trim()),
                    Integer.parseInt(cupoMax.getText().trim()), (Categoria) categoria.getSelectedItem(),
                    Double.parseDouble(precio.getText().trim().replace(',', '.')));
            actualizar();
            FormatoUI.informar(this, "Tour creado", "Se creo el tour \"" + tour.getNombre() + "\" con el codigo "
                    + tour.getCodigo() + ".");
        } catch (NumberFormatException ex) {
            FormatoUI.avisar(this, "Dias, cupos y precio tienen que ser numeros.");
        } catch (ReglaDeNegocioException ex) {
            FormatoUI.avisar(this, ex.getMessage());
        }
    }
}
