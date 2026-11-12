package destinosonado.ventanas;

import java.time.LocalDate;
import java.util.List;

import javax.swing.SwingUtilities;

import destinosonado.Empleados.Vendedor;
import destinosonado.TourDePrueba;
import destinosonado.Vista.FormularioReserva;
import destinosonado.controlador.ControladorReservas;
import destinosonado.controlador.RepositorioEnMemoria;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.servicios.CalculadoraPrecio;


public class PruebaFormularioReserva {

    public static void main(String[] args) {
        RepositorioEnMemoria<Salida> repoSalidas = new RepositorioEnMemoria<>(Salida::getId);
        RepositorioEnMemoria<Reserva> repoReservas = new RepositorioEnMemoria<>(Reserva::getId);

        Salida conSeguro = new Salida(1, new TourDePrueba(2, 4, 1000, true),
                LocalDate.now().plusDays(30), null, null);
        Salida sinSeguro = new Salida(2, new TourDePrueba(2, 6, 800, false),
                LocalDate.now().plusDays(90), null, null);
        repoSalidas.guardar(conSeguro);
        repoSalidas.guardar(sinSeguro);

        ControladorReservas controlador = new ControladorReservas(repoSalidas, repoReservas, new CalculadoraPrecio());
        Vendedor vendedor = new Vendedor("Ana", "Perez", "30111222", "11-0000-0000", 1, 0.05);

        SwingUtilities.invokeLater(() ->
                new FormularioReserva(controlador, List.of(conSeguro, sinSeguro), vendedor).setVisible(true));
    }
}