package destinosonado.Vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import destinosonado.Empleados.Vendedor;
import destinosonado.Pagos.Cancelacion.Cancelacion;
import destinosonado.Pagos.Pago;
import destinosonado.Pagos.PagoEfectivo;
import destinosonado.Pagos.PagoTarjeta;
import destinosonado.Pagos.PagoTransferencia;
import destinosonado.Persistencia.Repositorio;
import destinosonado.controlador.ControladorReservas;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Pasajero;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;

/**
 * Pestaña de reservas: lista las reservas del sistema y permite crear, pagar,
 * cancelar y ver el detalle. Solo llama al controlador; las reglas viven en el modelo.
 */
public class PanelReservas extends JPanel {

    private final ControladorReservas controlador;
    private final Repositorio<Reserva> repoReservas;
    private final Repositorio<Salida> repoSalidas;
    private final Vendedor vendedor;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Nro", "Cliente", "Tour", "Salida", "Pasajeros", "Total", "Pagado", "Saldo", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public PanelReservas(ControladorReservas controlador, Repositorio<Reserva> repoReservas,
                         Repositorio<Salida> repoSalidas, Vendedor vendedor) {
        this.controlador = controlador;
        this.repoReservas = repoReservas;
        this.repoSalidas = repoSalidas;
        this.vendedor = vendedor;

        JButton btnNueva = new JButton("Nueva reserva");
        JButton btnPago = new JButton("Registrar pago");
        JButton btnCancelar = new JButton("Cancelar reserva");
        JButton btnDetalle = new JButton("Ver detalle");
        JButton btnActualizar = new JButton("Actualizar");

        btnNueva.addActionListener(e -> nuevaReserva());
        btnPago.addActionListener(e -> registrarPago());
        btnCancelar.addActionListener(e -> cancelarReserva());
        btnDetalle.addActionListener(e -> verDetalle());
        btnActualizar.addActionListener(e -> actualizar());

        JPanel botones = new JPanel();
        botones.add(btnNueva);
        botones.add(btnPago);
        botones.add(btnCancelar);
        botones.add(btnDetalle);
        botones.add(btnActualizar);

        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        actualizar();
    }

    /** Vuelve a cargar la tabla; las reservas se ordenan por su orden natural (Comparable). */
    public void actualizar() {
        modelo.setRowCount(0);
        List<Reserva> reservas = new ArrayList<>(repoReservas.listarTodos());
        Collections.sort(reservas);
        for (Reserva r : reservas) {
            modelo.addRow(new Object[]{r.getNumero(), r.getCliente() == null ? "-" : r.getCliente().getNombreCompleto(),
                    r.getSalida().getTour().getNombre(), r.fechaSalida(), r.cantidadPasajeros(),
                    FormatoUI.plata(r.getTotal()), FormatoUI.plata(r.montoPagado()), FormatoUI.plata(r.saldo()),
                    r.getEstado()});
        }
    }

    private void nuevaReserva() {
        new FormularioReserva(controlador, repoSalidas.listarTodos(), vendedor,
                (numero, cliente, destino, cantidad) -> actualizar()).setVisible(true);
    }

    private Reserva seleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            FormatoUI.avisar(this, "Selecciona una reserva de la tabla.");
            return null;
        }
        int numero = (Integer) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        return repoReservas.buscarPorId(numero);
    }

    private void registrarPago() {
        Reserva reserva = seleccionada();
        if (reserva == null) {
            return;
        }
        JComboBox<String> medio = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta"});
        JTextField importe = new JTextField();
        JSpinner cuotas = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        cuotas.setEnabled(false);
        medio.addActionListener(e -> cuotas.setEnabled("Tarjeta".equals(medio.getSelectedItem())));

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Total de la reserva:"));
        form.add(new JLabel(FormatoUI.plata(reserva.getTotal())));
        form.add(new JLabel("Saldo pendiente:"));
        form.add(new JLabel(FormatoUI.plata(reserva.saldo())));
        form.add(new JLabel("Medio de pago:"));
        form.add(medio);
        form.add(new JLabel("Importe:"));
        form.add(importe);
        form.add(new JLabel("Cuotas (solo tarjeta):"));
        form.add(cuotas);

        int opcion = JOptionPane.showConfirmDialog(this, form, "Registrar pago - reserva " + reserva.getNumero(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            double monto = Double.parseDouble(importe.getText().trim().replace(',', '.'));
            if (monto <= 0) {
                throw new ReglaDeNegocioException("El importe debe ser mayor a 0");
            }
            Date hoy = new Date();
            Pago pago;
            switch ((String) medio.getSelectedItem()) {
                case "Transferencia":
                    pago = new PagoTransferencia(hoy, monto);
                    break;
                case "Tarjeta":
                    pago = new PagoTarjeta(hoy, monto, (Integer) cuotas.getValue());
                    break;
                default:
                    pago = new PagoEfectivo(hoy, monto);
            }
            Reserva actualizada = controlador.registrarPago(reserva.getNumero(), pago);
            actualizar();
            FormatoUI.informar(this, "Pago registrado", "Importe imputado: " + FormatoUI.plata(monto)
                    + "\nRecargo por cuotas: " + FormatoUI.plata(pago.calcularRecargo())
                    + "\nTotal a cobrar al cliente: " + FormatoUI.plata(pago.calcularTotal())
                    + "\n\nEstado de la reserva: " + actualizada.getEstado()
                    + "\nSaldo pendiente: " + FormatoUI.plata(actualizada.saldo()));
        } catch (NumberFormatException ex) {
            FormatoUI.avisar(this, "El importe tiene que ser un numero (por ejemplo 50000).");
        } catch (ReglaDeNegocioException ex) {
            FormatoUI.avisar(this, ex.getMessage());
        }
    }

    private void cancelarReserva() {
        Reserva reserva = seleccionada();
        if (reserva == null) {
            return;
        }
        JTextField motivo = new JTextField("El cliente no puede viajar");
        JCheckBox culpaAgencia = new JCheckBox("La cancelacion la provoca la agencia (se devuelve el 100 %)");

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.add(new JLabel("Reserva " + reserva.getNumero() + " - pagado hasta ahora: "
                + FormatoUI.plata(reserva.montoPagado())));
        form.add(new JLabel("Motivo de la cancelacion:"));
        form.add(motivo);
        form.add(culpaAgencia);

        int opcion = JOptionPane.showConfirmDialog(this, form, "Cancelar reserva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            if (motivo.getText().isBlank()) {
                throw new ReglaDeNegocioException("Escribi el motivo de la cancelacion");
            }
            Cancelacion c = controlador.cancelarReserva(reserva.getNumero(), motivo.getText().trim(),
                    culpaAgencia.isSelected());
            actualizar();
            FormatoUI.informar(this, "Reserva cancelada", "La reserva " + reserva.getNumero()
                    + " quedo ANULADA.\nImporte devuelto: " + FormatoUI.plata(c.getImporteDevuelto())
                    + "\nLos lugares vuelven al cupo de la salida.");
        } catch (ReglaDeNegocioException ex) {
            FormatoUI.avisar(this, ex.getMessage());
        }
    }

    private void verDetalle() {
        Reserva r = seleccionada();
        if (r == null) {
            return;
        }
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva ").append(r.getNumero()).append(" - ").append(r.getEstado()).append('\n');
        sb.append("Cliente: ").append(r.getCliente() == null ? "-" : r.getCliente().getNombreCompleto()).append('\n');
        sb.append("Vendedor: ").append(r.getVendedor() == null ? "-" : r.getVendedor().getNombreCompleto()).append('\n');
        sb.append("Tour: ").append(r.getSalida().getTour().getNombre()).append(" - sale ").append(r.fechaSalida()).append("\n\n");
        sb.append("Pasajeros:\n");
        for (Pasajero p : r.getPasajeros()) {
            sb.append("  - ").append(p.getNombreCompleto()).append(" (").append(p.getTipo()).append(", DNI ")
                    .append(p.getDocumento()).append(")");
            if (!p.getServicios().isEmpty()) {
                sb.append(" + ").append(p.getServicios().size()).append(" servicio(s) adicional(es)");
            }
            sb.append('\n');
        }
        sb.append("\nPagos:\n");
        if (r.getPagos().isEmpty()) {
            sb.append("  (sin pagos)\n");
        }
        for (Pago p : r.getPagos()) {
            sb.append("  - ").append(fecha.format(p.getFecha())).append(" ").append(p.getClass().getSimpleName())
                    .append(": ").append(FormatoUI.plata(p.getImporte()))
                    .append(" (recargo ").append(FormatoUI.plata(p.calcularRecargo())).append(")\n");
        }
        sb.append("\nTotal: ").append(FormatoUI.plata(r.getTotal()))
                .append(" | Pagado: ").append(FormatoUI.plata(r.montoPagado()))
                .append(" | Saldo: ").append(FormatoUI.plata(r.saldo())).append('\n');
        if (r.getCancelacion() != null) {
            sb.append("\nCancelada el ").append(r.getCancelacion().getFecha()).append(": ")
                    .append(r.getCancelacion().getMotivo()).append(" - devuelto ")
                    .append(FormatoUI.plata(r.getCancelacion().getImporteDevuelto())).append('\n');
        }
        FormatoUI.mostrarTexto(this, "Detalle de la reserva " + r.getNumero(), sb.toString());
    }
}
