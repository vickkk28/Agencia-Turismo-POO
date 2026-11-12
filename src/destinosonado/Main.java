package destinosonado;

import destinosonado.Empleados.Vendedor;
import destinosonado.Persistencia.Repositorio;
import destinosonado.Persistencia.RepositorioMemoria;
import destinosonado.Vista.VentanaPrincipal;
import destinosonado.controlador.ControladorReservas;
import destinosonado.model.Guia;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.model.Tarifa;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.Categoria;
import destinosonado.modelo.enums.Temporada;
import destinosonado.modelo.enums.TipoVehiculo;
import destinosonado.servicios.CalculadoraPrecio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Vendedor
        Vendedor vendedor = new Vendedor("Ana", "Lopez", "30111222", "3764000000", 1, 0.05);

        // 2. Repositorios (en memoria) y calculadora
        Repositorio<Salida> repoSalidas = new RepositorioMemoria<>(Salida::getId);
        Repositorio<Reserva> repoReservas = new RepositorioMemoria<>(Reserva::getNumero);
        CalculadoraPrecio calculadora = new CalculadoraPrecio();

        // 3. Controlador
        ControladorReservas controlador = new ControladorReservas(repoSalidas, repoReservas, calculadora);

        // 4. Tour (CULTURAL, para que no exija seguro en las pruebas)
        List<Tarifa> tarifas = new ArrayList<>();
        tarifas.add(new Tarifa(Temporada.MEDIA,
                LocalDate.now().minusYears(1), LocalDate.now().plusYears(2), 100000));

        Tour tour = new Tour("T01", "Cataratas del Iguazú", "Tour de 3 días", 3, 2, 20,
                new ArrayList<>(), new ArrayList<>(), tarifas, Categoria.CULTURAL);

        // 5. Guía y vehículo
        Guia guia = new Guia("Carlos", "Gomez", "28999888", "3764111111", 2,
                List.of("Español", "Inglés"), List.of(Categoria.CULTURAL), 20000);
        Vehiculo vehiculo = new Vehiculo("AB123CD", 20, TipoVehiculo.MICRO);

        // 6. Salidas
        Salida s1 = tour.programarSalida(1, LocalDate.now().plusDays(30), guia, vehiculo);
        Salida s2 = tour.programarSalida(2, LocalDate.now().plusDays(60), guia, vehiculo);
        repoSalidas.guardar(s1);
        repoSalidas.guardar(s2);
        List<Salida> salidas = new ArrayList<>(List.of(s1, s2));

        // 7. Ventana (la tabla se carga sola desde reservas.txt)
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(controlador, salidas, vendedor);

            JFrame frame = new JFrame("Agencia de Turismo - Principal");
            frame.setContentPane(ventana.getPanelPrincipal());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(850, 550);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}