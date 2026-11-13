package destinosonado.Vista;

import java.awt.Component;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import destinosonado.Empleados.Vendedor;
import destinosonado.Persistencia.Repositorio;
import destinosonado.controlador.ControladorReservas;
import destinosonado.controlador.ControladorSalidas;
import destinosonado.controlador.ControladorTours;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.model.Tour;
import destinosonado.servicios.ServicioReportes;

/** Ventana unica del sistema: una pestaña por area (reservas, salidas, tours, reportes). */
public class VentanaGestion extends JFrame {

    public VentanaGestion(ControladorReservas ctrlReservas, ControladorSalidas ctrlSalidas,
                          ControladorTours ctrlTours, ServicioReportes reportes,
                          Repositorio<Reserva> repoReservas, Repositorio<Salida> repoSalidas,
                          Repositorio<Tour> repoTours, Vendedor vendedor) {
        super("Destino Soñado - Gestion de la agencia");

        List<Salida> salidas = repoSalidas.listarTodos();
        VentanaPrincipal principal = new VentanaPrincipal(ctrlReservas, salidas, vendedor);
        PanelReservas panelReservas = new PanelReservas(ctrlReservas, repoReservas, repoSalidas, vendedor);
        PanelSalidas panelSalidas = new PanelSalidas(ctrlSalidas, repoSalidas, repoTours);
        PanelTours panelTours = new PanelTours(ctrlTours);
        PanelReportes panelReportes = new PanelReportes(reportes);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Reservas (gestion)", panelReservas);
        pestanias.addTab("Salidas", panelSalidas);
        pestanias.addTab("Tours", panelTours);
        pestanias.addTab("Reportes", panelReportes);
        pestanias.addTab("Tabla de reservas", principal.getPanelPrincipal());

        // Al cambiar de pestaña se vuelve a leer lo que muestra, asi siempre esta al dia.
        pestanias.addChangeListener(e -> {
            Component actual = pestanias.getSelectedComponent();
            if (actual == panelReservas) {
                panelReservas.actualizar();
            } else if (actual == panelSalidas) {
                panelSalidas.actualizar();
            } else if (actual == panelTours) {
                panelTours.actualizar();
            }
        });

        setContentPane(pestanias);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
}
