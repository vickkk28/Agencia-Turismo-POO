package destinosonado;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import javax.swing.SwingUtilities;

import destinosonado.Empleados.Vendedor;
import destinosonado.Persistencia.AlmacenArchivo;
import destinosonado.Persistencia.Repositorio;
import destinosonado.Persistencia.RepositorioArchivo;
import destinosonado.Vista.FormularioReserva;
import destinosonado.controlador.ControladorReservas;
import destinosonado.controlador.ControladorSalidas;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Guia;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.model.Tarifa;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.Categoria;
import destinosonado.modelo.enums.Temporada;
import destinosonado.modelo.enums.TipoVehiculo;
import destinosonado.servicios.CalculadoraPrecio;
import destinosonado.servicios.ServicioReportes;

/**
 * Punto de entrada. Arma los repositorios (con persistencia en archivo) y los
 * controladores, carga datos de prueba la primera vez y abre la ventana de reservas.
 * Los datos se guardan en datos/agencia.dat y se conservan entre ejecuciones.
 */
public class Main {

    private static final String RUTA_DATOS = "datos/agencia.dat";

    public static void main(String[] args) {
        Repositorio<Tour> repoTours;
        Repositorio<Salida> repoSalidas;
        Repositorio<Reserva> repoReservas;
        ControladorSalidas controladorSalidas;

        try {
            AlmacenArchivo almacen = abrirAlmacen();
            // El codigo de cada tour es numerico y hace de id (ej: "1")
            repoTours = new RepositorioArchivo<>(almacen, "tours", t -> Integer.parseInt(t.getCodigo()));
            repoSalidas = new RepositorioArchivo<>(almacen, "salidas", Salida::getId);
            repoReservas = new RepositorioArchivo<>(almacen, "reservas", Reserva::getId);
            controladorSalidas = new ControladorSalidas(repoTours, repoSalidas, new LiquidadorHonorarios());

            if (repoTours.listarTodos().isEmpty()) {
                cargarDatosDePrueba(repoTours, controladorSalidas);
            }
        } catch (ReglaDeNegocioException | IllegalStateException e) {
            System.out.println("No se pudo iniciar el sistema: " + e.getMessage());
            return;
        } finally {
            System.out.println("Agencia de Turismo - Destino Soñado");
        }

        System.out.println("Datos cargados: " + repoTours.listarTodos().size() + " tours, "
                + repoSalidas.listarTodos().size() + " salidas, "
                + repoReservas.listarTodos().size() + " reservas.");
        System.out.println("--- Ocupacion por salida ---");
        new ServicioReportes(repoSalidas, repoReservas).ocupacionPorSalida().forEach(System.out::println);

        ControladorReservas controladorReservas =
                new ControladorReservas(repoSalidas, repoReservas, new CalculadoraPrecio());
        Vendedor vendedor = new Vendedor("Ana", "Perez", "30111222", "11-4000-0001", 1, 0.05);
        List<Salida> salidas = repoSalidas.listarTodos();

        SwingUtilities.invokeLater(() ->
                new FormularioReserva(controladorReservas, salidas, vendedor).setVisible(true));
    }

    /** Abre el archivo de datos. Si esta danado, lo aparta (.danado) y empieza de cero. */
    private static AlmacenArchivo abrirAlmacen() {
        try {
            return new AlmacenArchivo(RUTA_DATOS);
        } catch (IllegalStateException e) {
            System.out.println("El archivo de datos estaba danado; se guarda aparte y se empieza de cero.");
            new File(RUTA_DATOS).renameTo(new File(RUTA_DATOS + ".danado"));
            return new AlmacenArchivo(RUTA_DATOS);
        }
    }

    /** Datos de ejemplo: tres tours con sus tarifas y una salida de cada uno. */
    private static void cargarDatosDePrueba(Repositorio<Tour> repoTours, ControladorSalidas salidas)
            throws ReglaDeNegocioException {
        LocalDate desde = LocalDate.of(2026, 1, 1);
        LocalDate hasta = LocalDate.of(2027, 12, 31);

        repoTours.guardar(new Tour("1", "Cataratas del Iguazu", "Circuito por las cataratas argentinas y brasilenas",
                4, 4, 12, new java.util.ArrayList<>(), new java.util.ArrayList<>(),
                tarifa(desde, hasta, 180000), Categoria.NATURALEZA));
        repoTours.guardar(new Tour("2", "Trekking Fitz Roy", "Caminata de varios dias por El Chalten",
                5, 2, 10, new java.util.ArrayList<>(), new java.util.ArrayList<>(),
                tarifa(desde, hasta, 250000), Categoria.AVENTURA));
        repoTours.guardar(new Tour("3", "Salta Colonial", "Recorrido cultural por la ciudad y los valles",
                3, 3, 15, new java.util.ArrayList<>(), new java.util.ArrayList<>(),
                tarifa(desde, hasta, 120000), Categoria.CULTURAL));

        Guia guia = new Guia("Carlos", "Rios", "27333444", "11-4000-0002", 2,
                List.of("Espanol", "Ingles"), List.of(Categoria.values()), 15000);
        Vehiculo micro = new Vehiculo("AB123CD", 15, TipoVehiculo.MICRO);

        LocalDate hoy = LocalDate.now();
        salidas.programarSalida(1, hoy.plusDays(20), guia, micro);
        salidas.programarSalida(2, hoy.plusDays(45), guia, micro);
        salidas.programarSalida(3, hoy.plusDays(90), guia, micro);
    }

    private static List<Tarifa> tarifa(LocalDate desde, LocalDate hasta, double precio) {
        return new java.util.ArrayList<>(List.of(new Tarifa(Temporada.MEDIA, desde, hasta, precio)));
    }
}
