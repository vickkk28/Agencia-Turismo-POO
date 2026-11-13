package destinosonado.Vista;

import destinosonado.Empleados.Vendedor;
import destinosonado.Persistencia.ArchivosReservastxt;
import destinosonado.controlador.ControladorReservas;
import destinosonado.model.Salida;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VentanaPrincipal {
    private static final int COL_ID = 0;
    private static final int COL_ESTADO = 4;

    private ControladorReservas controlador;
    private List<Salida> listaSalidas;
    private Vendedor vendedorActual;
    private JTable TablaReservas;
    private JButton btnNuevo;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTextField textBuscar;
    private JPanel panelPrincipal;

    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private final ArchivosReservastxt archivo = new ArchivosReservastxt("reservas.txt");

    public VentanaPrincipal() {
        this(null, null, null);
    }

    public VentanaPrincipal(ControladorReservas controlador, List<Salida> salidas, Vendedor vendedor) {
        this.controlador = controlador;
        this.listaSalidas = (salidas != null) ? salidas : new ArrayList<>();
        this.vendedorActual = vendedor;

        // Con el disenador de IntelliJ (VentanaPrincipal.form) los componentes ya vienen creados.
        // Si se ejecuta desde otro entorno (por ejemplo VS Code) no existen, y se arman a mano.
        crearComponentesSiFaltan();

        configurarTabla();
        sorter = new TableRowSorter<>(modelo);
        TablaReservas.setRowSorter(sorter);

        // ---------- NUEVO ----------
        btnNuevo.addActionListener(e -> {
            FormularioReserva form = new FormularioReserva(controlador, listaSalidas, vendedorActual,
                    (numero, cliente, destino, cantidad) -> {
                        modelo.addRow(new Object[]{siguienteId(), cliente, destino, cantidad, "Pendiente"});
                        guardarTxt();
                    });
            form.setVisible(true);
        });

        // ---------- MODIFICAR ----------
        btnModificar.addActionListener(e -> {
            int filaVista = TablaReservas.getSelectedRow();
            if (filaVista == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Seleccioná una reserva de la tabla.");
                return;
            }
            final int filaModelo = TablaReservas.convertRowIndexToModel(filaVista);
            Object id = modelo.getValueAt(filaModelo, COL_ID);
            String estadoActual = String.valueOf(modelo.getValueAt(filaModelo, COL_ESTADO));

            FormularioModificar form = new FormularioModificar(controlador, vendedorActual, id, estadoActual,
                    nuevoEstado -> {
                        modelo.setValueAt(nuevoEstado, filaModelo, COL_ESTADO);
                        guardarTxt();
                    });
            form.setVisible(true);
        });

        // ---------- ELIMINAR ----------
        btnEliminar.addActionListener(e -> {
            int filaVista = TablaReservas.getSelectedRow();
            if (filaVista == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Seleccioná una reserva para eliminar.");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Seguro que querés eliminar la reserva?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            int filaModelo = TablaReservas.convertRowIndexToModel(filaVista);
            modelo.removeRow(filaModelo);
            guardarTxt();
        });

        // ---------- BUSCAR ----------
        btnBuscar.addActionListener(e -> {
            String texto = textBuscar.getText().trim();
            sorter.setRowFilter(texto.isEmpty() ? null
                    : RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        });
    }

    /** Arma la ventana por codigo cuando el disenador de formularios de IntelliJ no la creo. */
    private void crearComponentesSiFaltan() {
        if (TablaReservas != null) {
            return;
        }
        TablaReservas = new JTable();
        btnNuevo = new JButton("Nuevo");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnBuscar = new JButton("Buscar");
        textBuscar = new JTextField(20);
        panelPrincipal = new JPanel(new BorderLayout(5, 5));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.add(textBuscar);
        busqueda.add(btnBuscar);

        JPanel botones = new JPanel();
        botones.add(btnNuevo);
        botones.add(btnModificar);
        botones.add(btnEliminar);

        panelPrincipal.add(busqueda, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(TablaReservas), BorderLayout.CENTER);
        panelPrincipal.add(botones, BorderLayout.SOUTH);
    }

    private void configurarTabla() {
        String[] columnas = {"ID Reserva", "Pasajero", "Destino / Tour", "Cupos", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        TablaReservas.setModel(modelo);
        cargarTxt();
    }

    //lena la tabla con lo que haya en reservas.txt
    private void cargarTxt() {
        for (String[] fila : archivo.leer()) {
            modelo.addRow(new Object[]{fila[0], fila[1], fila[2], fila[3], fila[4]});
        }
    }

    //Escribe en reservas.txt todas las filas actuales de la tabla.
    private void guardarTxt() {
        List<String[]> filas = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String[] fila = new String[modelo.getColumnCount()];
            for (int c = 0; c < fila.length; c++) {
                fila[c] = String.valueOf(modelo.getValueAt(i, c));
            }
            filas.add(fila);
        }
        archivo.escribir(filas);
    }

    private String siguienteId() {
        int max = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                max = Math.max(max, Integer.parseInt(String.valueOf(modelo.getValueAt(i, COL_ID)).trim()));
            } catch (NumberFormatException ignorado) { }
        }
        return String.format("%03d", max + 1);
    }

    public JPanel getPanelPrincipal() { return panelPrincipal; }
}