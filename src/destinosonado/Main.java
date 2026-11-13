package destinosonado;

import destinosonado.Empleados.Vendedor;
import destinosonado.Persistencia.AlmacenArchivo;
import destinosonado.Persistencia.Repositorio;
import destinosonado.Persistencia.RepositorioArchivo;
import destinosonado.Vista.VentanaGestion;
import destinosonado.controlador.ControladorReservas;
import destinosonado.controlador.ControladorSalidas;
import destinosonado.controlador.ControladorTours;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Guia;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.Categoria;
import destinosonado.modelo.enums.TipoVehiculo;
import destinosonado.servicios.CalculadoraPrecio;
import destinosonado.servicios.ServicioReportes;

import javax.swing.SwingUtilities;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String RUTA_DATOS = "datos/agencia.dat";

    public static void main(String[] args) {

        Vendedor vendedor = new Vendedor("Ana", "Lopez", "30111222", "3764000000", 1, 0.05);

        // Repositorios con persistencia en archivo
        AlmacenArchivo almacen = abrirAlmacen();
        Repositorio<Tour> repoTours = new RepositorioArchivo<>(almacen, "tours", ControladorTours::idDe);
        Repositorio<Salida> repoSalidas = new RepositorioArchivo<>(almacen, "salidas", Salida::getId);
        Repositorio<Reserva> repoReservas = new RepositorioArchivo<>(almacen, "reservas", Reserva::getNumero);

        // Controladores y servicios
        ControladorTours ctrlTours = new ControladorTours(repoTours);
        ControladorReservas ctrlReservas = new ControladorReservas(repoSalidas, repoReservas, new CalculadoraPrecio());
        ControladorSalidas ctrlSalidas = new ControladorSalidas(repoTours, repoSalidas, new LiquidadorHonorarios());
        ServicioReportes reportes = new ServicioReportes(repoSalidas, repoReservas);

        // Solo la primera vez (archivo vacio) se cargan los datos de prueba.
        if (repoTours.listarTodos().isEmpty()) {
            cargarDatosDePrueba(ctrlTours, ctrlSalidas);
        }
        System.out.println("Datos cargados: " + repoTours.listarTodos().size() + " tours, "
                + repoSalidas.listarTodos().size() + " salidas, "
                + repoReservas.listarTodos().size() + " reservas.");

        SwingUtilities.invokeLater(() -> new VentanaGestion(ctrlReservas, ctrlSalidas, ctrlTours, reportes,
                repoReservas, repoSalidas, repoTours, vendedor).setVisible(true));
    }

    /** Tres tours (uno por categoria) y cuatro salidas, usando los mismos controladores que la interfaz. */
    private static void cargarDatosDePrueba(ControladorTours ctrlTours, ControladorSalidas ctrlSalidas) {
        try {
            Tour iguazu = ctrlTours.crearTour("Cataratas del Iguazu", "Tour de 3 dias", 3, 2, 20,
                    Categoria.CULTURAL, 100000);
            Tour trekking = ctrlTours.crearTour("Trekking Fitz Roy", "Aventura de 4 dias", 4, 4, 12,
                    Categoria.AVENTURA, 150000);
            Tour esteros = ctrlTours.crearTour("Esteros del Ibera", "Naturaleza de 2 dias", 2, 2, 10,
                    Categoria.NATURALEZA, 80000);

            Guia carlos = new Guia("Carlos", "Gomez", "28999888", "3764111111", 2,
                    List.of("Español", "Inglés"), List.of(Categoria.CULTURAL, Categoria.NATURALEZA), 20000);
            Guia laura = new Guia("Laura", "Perez", "27555444", "3764222222", 3,
                    List.of("Español", "Portugués"), List.of(Categoria.AVENTURA, Categoria.NATURALEZA), 25000);
            Vehiculo micro = new Vehiculo("AB123CD", 20, TipoVehiculo.MICRO);
            Vehiculo combi = new Vehiculo("CD456EF", 12, TipoVehiculo.COMBI);

            LocalDate hoy = LocalDate.now();
            ctrlSalidas.programarSalida(ControladorTours.idDe(iguazu), hoy.plusDays(30), carlos, micro);
            ctrlSalidas.programarSalida(ControladorTours.idDe(iguazu), hoy.plusDays(60), carlos, micro);
            ctrlSalidas.programarSalida(ControladorTours.idDe(trekking), hoy.plusDays(75), laura, combi);
            ctrlSalidas.programarSalida(ControladorTours.idDe(esteros), hoy.plusDays(20), laura, combi);
        } catch (ReglaDeNegocioException e) {
            throw new IllegalStateException("Los datos de prueba no cumplen las reglas: " + e.getMessage(), e);
        }
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
}
