package destinosonado.Vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import destinosonado.Liquidacion;
import destinosonado.Persistencia.Repositorio;
import destinosonado.Vehiculo;
import destinosonado.controlador.ControladorSalidas;
import destinosonado.controlador.ControladorTours;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Guia;
import destinosonado.model.Pasajero;
import destinosonado.model.Salida;
import destinosonado.model.Tour;

/** Pestaña de salidas: programar, ver cupos, lista de pasajeros, liquidar honorarios y verificar salidas. */
public class PanelSalidas extends JPanel {

    private final ControladorSalidas controlador;
    private final Repositorio<Salida> repoSalidas;
    private final Repositorio<Tour> repoTours;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Tour", "Inicio", "Fin", "Guia", "Vehiculo", "Estado", "Lugares libres", "Reservas"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final JCheckBox ordenarPorCupo = new JCheckBox("Ordenar por lugares libres (mas libres primero)");

    public PanelSalidas(ControladorSalidas controlador, Repositorio<Salida> repoSalidas, Repositorio<Tour> repoTours) {
        this.controlador = controlador;
        this.repoSalidas = repoSalidas;
        this.repoTours = repoTours;

        JButton btnProgramar = new JButton("Programar salida");
        JButton btnLista = new JButton("Lista de pasajeros");
        JButton btnLiquidar = new JButton("Liquidar honorarios");
        JButton btnVerificar = new JButton("Verificar salidas");
        JButton btnActualizar = new JButton("Actualizar");

        btnProgramar.addActionListener(e -> programar());
        btnLista.addActionListener(e -> listaPasajeros());
        btnLiquidar.addActionListener(e -> liquidar());
        btnVerificar.addActionListener(e -> verificar());
        btnActualizar.addActionListener(e -> actualizar());
        ordenarPorCupo.addActionListener(e -> actualizar());

        JPanel arriba = new JPanel();
        arriba.add(ordenarPorCupo);
        JPanel botones = new JPanel();
        botones.add(btnProgramar);
        botones.add(btnLista);
        botones.add(btnLiquidar);
        botones.add(btnVerificar);
        botones.add(btnActualizar);

        setLayout(new BorderLayout(5, 5));
        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        actualizar();
    }

    public void actualizar() {
        modelo.setRowCount(0);
        List<Salida> salidas = new ArrayList<>(repoSalidas.listarTodos());
        if (ordenarPorCupo.isSelected()) {
            salidas.sort(Salida.POR_CUPO_DISPONIBLE);
        }
        for (Salida s : salidas) {
            modelo.addRow(new Object[]{s.getId(), s.getTour().getNombre(), s.getFechaInicio(), s.getFechaFin(),
                    s.getGuia() == null ? "-" : s.getGuia().getNombreCompleto(),
                    s.getVehiculo() == null ? "-" : s.getVehiculo().getPatente(),
                    s.getEstado(), s.lugaresDisponibles(), s.getReservas().size()});
        }
    }

    private Salida seleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            FormatoUI.avisar(this, "Selecciona una salida de la tabla.");
            return null;
        }
        return repoSalidas.buscarPorId((Integer) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0));
    }

    private void programar() {
        List<Tour> tours = repoTours.listarTodos();
        if (tours.isEmpty()) {
            FormatoUI.avisar(this, "Primero hay que crear un tour (pestaña Tours).");
            return;
        }
        // Guias y vehiculos conocidos: los que ya figuran en alguna salida.
        Map<String, Guia> guias = new LinkedHashMap<>();
        Map<String, Vehiculo> vehiculos = new LinkedHashMap<>();
        for (Salida s : repoSalidas.listarTodos()) {
            if (s.getGuia() != null) {
                guias.putIfAbsent(s.getGuia().getDocumento(), s.getGuia());
            }
            if (s.getVehiculo() != null) {
                vehiculos.putIfAbsent(s.getVehiculo().getPatente(), s.getVehiculo());
            }
        }
        JComboBox<String> comboTour = new JComboBox<>();
        for (Tour t : tours) {
            comboTour.addItem(t.getCodigo() + " - " + t.getNombre() + " (" + t.getCategoria() + ")");
        }
        JComboBox<String> comboGuia = new JComboBox<>();
        List<Guia> listaGuias = new ArrayList<>(guias.values());
        for (Guia g : listaGuias) {
            comboGuia.addItem(g.getNombreCompleto() + " - habilitado en " + g.getCategoriasHabilitadas());
        }
        JComboBox<String> comboVehiculo = new JComboBox<>();
        List<Vehiculo> listaVehiculos = new ArrayList<>(vehiculos.values());
        for (Vehiculo v : listaVehiculos) {
            comboVehiculo.addItem(v.getPatente() + " - capacidad " + v.getCapacidad());
        }
        JTextField fecha = new JTextField(LocalDate.now().plusDays(90).toString());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Tour:"));
        form.add(comboTour);
        form.add(new JLabel("Fecha de inicio (AAAA-MM-DD):"));
        form.add(fecha);
        form.add(new JLabel("Guia:"));
        form.add(comboGuia);
        form.add(new JLabel("Vehiculo:"));
        form.add(comboVehiculo);

        int opcion = JOptionPane.showConfirmDialog(this, form, "Programar salida",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            if (listaGuias.isEmpty() || listaVehiculos.isEmpty()) {
                throw new ReglaDeNegocioException("No hay guias o vehiculos cargados");
            }
            Tour tour = tours.get(comboTour.getSelectedIndex());
            Salida s = controlador.programarSalida(ControladorTours.idDe(tour),
                    LocalDate.parse(fecha.getText().trim()),
                    listaGuias.get(comboGuia.getSelectedIndex()),
                    listaVehiculos.get(comboVehiculo.getSelectedIndex()));
            actualizar();
            FormatoUI.informar(this, "Salida programada", "Salida " + s.getId() + " de \"" + tour.getNombre()
                    + "\" del " + s.getFechaInicio() + " al " + s.getFechaFin() + ".");
        } catch (DateTimeParseException ex) {
            FormatoUI.avisar(this, "La fecha tiene que escribirse asi: 2027-01-31");
        } catch (ReglaDeNegocioException ex) {
            FormatoUI.avisar(this, ex.getMessage());
        }
    }

    private void listaPasajeros() {
        Salida s = seleccionada();
        if (s == null) {
            return;
        }
        try {
            List<Pasajero> lista = controlador.emitirListaPasajeros(s.getId());
            StringBuilder sb = new StringBuilder("Salida " + s.getId() + " - " + s.getTour().getNombre()
                    + " - " + s.getFechaInicio() + "\n\n");
            if (lista.isEmpty()) {
                sb.append("(todavia no hay pasajeros confirmados)");
            }
            int n = 1;
            for (Pasajero p : lista) {
                sb.append(n++).append(". ").append(p.getNombreCompleto()).append(" - DNI ").append(p.getDocumento())
                        .append(" - ").append(p.getTipo()).append('\n');
            }
            FormatoUI.mostrarTexto(this, "Lista de pasajeros", sb.toString());
        } catch (ReglaDeNegocioException ex) {
            FormatoUI.avisar(this, ex.getMessage());
        }
    }

    private void liquidar() {
        Salida s = seleccionada();
        if (s == null) {
            return;
        }
        try {
            Liquidacion l = controlador.liquidarSalida(s.getId());
            FormatoUI.informar(this, "Liquidacion de honorarios", "Guia: " + s.getGuia().getNombreCompleto()
                    + "\nDias trabajados: " + l.getDiasTrabajados()
                    + "\nMonto a pagar: " + FormatoUI.plata(l.getMonto())
                    + "\nFecha: " + l.getFecha());
        } catch (ReglaDeNegocioException ex) {
            FormatoUI.avisar(this, ex.getMessage());
        }
    }

    private void verificar() {
        controlador.verificarSalidas();
        actualizar();
        FormatoUI.informar(this, "Verificacion de salidas",
                "Se revisaron todas las salidas: las que llegaron al cupo minimo quedaron CONFIRMADAS\n"
                        + "y las que no lo alcanzaron a tiempo se actualizaron segun corresponde.");
    }
}
