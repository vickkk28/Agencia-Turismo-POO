package destinosonado.informes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import destinosonado.DatosPasajero;
import destinosonado.Empleados.Vendedor;
import destinosonado.Pagos.PagoEfectivo;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.controlador.ControladorReservas;
import destinosonado.controlador.RepositorioEnMemoria;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.modelo.enums.TipoPasajero;
import destinosonado.servicios.CalculadoraPrecio;
import destinosonado.servicios.ServicioReportes;

/* Orden natural (Comparable), orden personalizado (Comparator) y reportes. */
public class ReportesYOrdenTest {

    private static List<DatosPasajero> adultos(int n) {
        List<DatosPasajero> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lista.add(new DatosPasajero("P" + i, "Prueba", "3000000" + i, "11-1111-1111",
                    LocalDate.of(1990, 1, 1), TipoPasajero.ADULTO, "11-5555-0000"));
        }
        return lista;
    }

    public static void main(String[] args) throws Exception {
        RepositorioEnMemoria<Salida> repoSalidas = new RepositorioEnMemoria<>(Salida::getId);
        RepositorioEnMemoria<Reserva> repoReservas = new RepositorioEnMemoria<>(Reserva::getId);
        Salida tarde = new Salida(1, new TourDePrueba(2, 10, 1000, false), LocalDate.now().plusDays(50), null, null);
        Salida temprano = new Salida(2, new TourDePrueba(2, 4, 500, false), LocalDate.now().plusDays(20), null, null);
        repoSalidas.guardar(tarde);
        repoSalidas.guardar(temprano);
        ControladorReservas controlador = new ControladorReservas(repoSalidas, repoReservas, new CalculadoraPrecio());
        Vendedor ana = new Vendedor("Ana", "Perez", "30111222", "11-0000-0000", 1, 0.05);
        Vendedor luis = new Vendedor("Luis", "Diaz", "30333444", "11-0000-0001", 2, 0.05);

        Reserva paraTarde = controlador.crearReserva(1, null, ana, adultos(2));     // total 2000
        Reserva paraTemprano = controlador.crearReserva(2, null, luis, adultos(1)); // total 500
        controlador.registrarPago(paraTarde.getId(), new PagoEfectivo(new Date(), 2000));
        controlador.registrarPago(paraTemprano.getId(), new PagoEfectivo(new Date(), 500));

        // Comparable: la reserva de la salida mas cercana va primero
        List<Reserva> reservas = new ArrayList<>(List.of(paraTarde, paraTemprano));
        Collections.sort(reservas);
        Verificador.igual("ordenadas por fecha de salida va primero la de la salida mas cercana", 2, reservas.get(0).getId());

        // Comparator: primero la salida con mas lugares libres
        List<Salida> salidas = new ArrayList<>(List.of(temprano, tarde));
        salidas.sort(Salida.POR_CUPO_DISPONIBLE);
        Verificador.igual("primero la salida con mas lugares libres (8 contra 3)", 1, salidas.get(0).getId());

        ServicioReportes reportes = new ServicioReportes(repoSalidas, repoReservas);
        Verificador.igual("hay una linea de ocupacion por salida", 2, reportes.ocupacionPorSalida().size());
        Verificador.igualTexto("el vendedor que mas vendio aparece primero", "Ana Perez: $2000.0", reportes.ventasPorVendedor().get(0));
        Verificador.igualTexto("ingresos por tour: se suma lo cobrado de las dos reservas (2000 + 500)", "Tour de prueba: $2500.0", reportes.ingresosPorTour().get(0));
        Verificador.igual("sin cancelaciones el reporte esta vacio", 0, reportes.cancelaciones().size());

        controlador.cancelarReserva(paraTemprano.getId(), "Cambio de planes", false);
        Verificador.igual("despues de cancelar aparece una cancelacion", 1, reportes.cancelaciones().size());
        Verificador.igual("las ventas ya no cuentan la reserva anulada", 1, reportes.ventasPorVendedor().size());

        Verificador.resumen();
    }
}
