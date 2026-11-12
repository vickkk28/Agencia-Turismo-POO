package destinosonado.Vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import destinosonado.Cliente;
import destinosonado.DatosPasajero;
import destinosonado.Empleados.Vendedor;
import destinosonado.ServicioAdicional;
import destinosonado.controlador.ControladorReservas;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.modelo.enums.TipoPasajero;

/**
 * Ventana "Nueva reserva". No calcula nada: junta lo que escribio el vendedor
 * y se lo pasa al ControladorReservas (patron Controlador). Las reglas de
 * negocio (cupo, seguro, precio) viven en el modelo, no aca.
 */
public class FormularioReserva extends JFrame {

    // Seguro que se agrega al pasajero cuando se tilda la casilla (precio de ejemplo).
    private static final ServicioAdicional SEGURO = new ServicioAdicional("Seguro de viaje", 5000, false);

    private final ControladorReservas controlador;
    private final Vendedor vendedor;

    // Salida
    private final JComboBox<Salida> cbSalidas = new JComboBox<>();

    // Cliente (quien contrata)
    private final JTextField txtClienteNombre = new JTextField();
    private final JTextField txtClienteApellido = new JTextField();
    private final JTextField txtClienteDni = new JTextField();
    private final JTextField txtClienteTelefono = new JTextField();
    private final JTextField txtClienteEmail = new JTextField();

    // Pasajero que se esta cargando
    private final JTextField txtNombrePasajero = new JTextField();
    private final JTextField txtApellidoPasajero = new JTextField();
    private final JTextField txtDniPasajero = new JTextField();
    private final JTextField txtTelefonoPasajero = new JTextField();
    private final JTextField txtNacimiento = new JTextField("1990-01-31");
    private final JComboBox<TipoPasajero> cbTipo = new JComboBox<>(TipoPasajero.values());
    private final JTextField txtEmergencia = new JTextField();
    private final JCheckBox chkSeguro = new JCheckBox("Contrata seguro de viaje");

    // Pasajeros ya agregados a la reserva
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final List<DatosPasajero> pasajeros = new ArrayList<>();

    private final JButton btnAgregar = new JButton("Agregar pasajero");
    private final JButton btnGuardar = new JButton("Guardar reserva");
    private final JButton btnCancelar = new JButton("Cancelar");

    public FormularioReserva(ControladorReservas controlador, List<Salida> salidas, Vendedor vendedor) {
        this.controlador = controlador;
        this.vendedor = vendedor;

        setTitle("Nueva reserva");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        for (Salida s : salidas) {
            cbSalidas.addItem(s);
        }
        cbSalidas.setRenderer((lista, s, i, sel, foco) -> new JLabel(s == null ? "" :
                s.getTour().getNombre() + " - sale " + s.getFechaInicio()
                        + " (" + s.lugaresDisponibles() + " lugares)"));

        armarVentana();
        btnAgregar.addActionListener(e -> agregarPasajero());
        btnGuardar.addActionListener(e -> guardarReserva());
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void armarVentana() {
        JPanel arriba = new JPanel(new GridLayout(0, 2, 5, 5));
        arriba.setBorder(BorderFactory.createTitledBorder("Salida y cliente"));
        arriba.add(new JLabel("Salida:"));
        arriba.add(cbSalidas);
        arriba.add(new JLabel("Cliente - nombre:"));
        arriba.add(txtClienteNombre);
        arriba.add(new JLabel("Cliente - apellido:"));
        arriba.add(txtClienteApellido);
        arriba.add(new JLabel("Cliente - DNI:"));
        arriba.add(txtClienteDni);
        arriba.add(new JLabel("Cliente - telefono:"));
        arriba.add(txtClienteTelefono);
        arriba.add(new JLabel("Cliente - email:"));
        arriba.add(txtClienteEmail);

        JPanel centro = new JPanel(new GridLayout(0, 2, 5, 5));
        centro.setBorder(BorderFactory.createTitledBorder("Pasajero"));
        centro.add(new JLabel("Nombre:"));
        centro.add(txtNombrePasajero);
        centro.add(new JLabel("Apellido:"));
        centro.add(txtApellidoPasajero);
        centro.add(new JLabel("DNI:"));
        centro.add(txtDniPasajero);
        centro.add(new JLabel("Telefono:"));
        centro.add(txtTelefonoPasajero);
        centro.add(new JLabel("Nacimiento (AAAA-MM-DD):"));
        centro.add(txtNacimiento);
        centro.add(new JLabel("Tipo de pasajero:"));
        centro.add(cbTipo);
        centro.add(new JLabel("Contacto de emergencia:"));
        centro.add(txtEmergencia);
        centro.add(chkSeguro);
        centro.add(btnAgregar);

        JScrollPane lista = new JScrollPane(new JList<>(modeloLista));
        lista.setBorder(BorderFactory.createTitledBorder("Pasajeros de la reserva"));
        lista.setPreferredSize(new java.awt.Dimension(300, 90));

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        JPanel medio = new JPanel(new BorderLayout());
        medio.add(centro, BorderLayout.NORTH);
        medio.add(lista, BorderLayout.CENTER);

        setLayout(new BorderLayout(5, 5));
        add(arriba, BorderLayout.NORTH);
        add(medio, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /** Lee los campos del pasajero, los valida y lo suma a la lista. */
    private void agregarPasajero() {
        try {
            if (txtNombrePasajero.getText().isBlank() || txtDniPasajero.getText().isBlank()) {
                throw new ReglaDeNegocioException("El pasajero necesita al menos nombre y DNI");
            }
            LocalDate nacimiento = LocalDate.parse(txtNacimiento.getText().trim());

            List<ServicioAdicional> servicios = new ArrayList<>();
            if (chkSeguro.isSelected()) {
                servicios.add(SEGURO);
            }
            DatosPasajero datos = new DatosPasajero(
                    txtNombrePasajero.getText().trim(), txtApellidoPasajero.getText().trim(),
                    txtDniPasajero.getText().trim(), txtTelefonoPasajero.getText().trim(),
                    nacimiento, (TipoPasajero) cbTipo.getSelectedItem(),
                    txtEmergencia.getText().trim(), servicios);

            pasajeros.add(datos);
            modeloLista.addElement(datos.getNombre() + " " + datos.getApellido()
                    + " - " + datos.getTipo() + (servicios.isEmpty() ? "" : " + seguro"));
            limpiarPasajero();
        } catch (DateTimeParseException ex) {
            avisar("La fecha de nacimiento debe tener el formato AAAA-MM-DD (ej: 1990-01-31)");
        } catch (ReglaDeNegocioException ex) {
            avisar(ex.getMessage());
        }
    }

    /** Arma el Cliente y le pide al controlador que cree la reserva. */
    private void guardarReserva() {
        try {
            Salida salida = (Salida) cbSalidas.getSelectedItem();
            if (salida == null) {
                throw new ReglaDeNegocioException("Elegi una salida");
            }
            if (txtClienteNombre.getText().isBlank() || txtClienteDni.getText().isBlank()) {
                throw new ReglaDeNegocioException("El cliente necesita al menos nombre y DNI");
            }
            Cliente cliente = new Cliente(txtClienteNombre.getText().trim(), txtClienteApellido.getText().trim(),
                    txtClienteDni.getText().trim(), txtClienteTelefono.getText().trim(),
                    txtClienteEmail.getText().trim(), "Argentina");

            Reserva reserva = controlador.crearReserva(salida.getId(), cliente, vendedor, pasajeros);

            JOptionPane.showMessageDialog(this, "Reserva N° " + reserva.getNumero() + " creada."
                    + "\nPasajeros: " + reserva.cantidadPasajeros()
                    + "\nTotal: $" + reserva.getTotal(), "Reserva creada", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (ReglaDeNegocioException ex) {
            // Cupo insuficiente, falta el seguro, salida cerrada, etc.
            avisar(ex.getMessage());
        }
    }

    private void limpiarPasajero() {
        txtNombrePasajero.setText("");
        txtApellidoPasajero.setText("");
        txtDniPasajero.setText("");
        txtTelefonoPasajero.setText("");
        txtEmergencia.setText("");
        chkSeguro.setSelected(false);
    }

    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "No se pudo continuar", JOptionPane.WARNING_MESSAGE);
    }
}