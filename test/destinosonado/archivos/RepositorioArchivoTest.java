package destinosonado.archivos;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import destinosonado.DatosPasajero;
import destinosonado.Pagos.PagoEfectivo;
import destinosonado.Persistencia.AlmacenArchivo;
import destinosonado.Persistencia.RepositorioArchivo;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.controlador.ControladorReservas;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.modelo.enums.EstadoReserva;
import destinosonado.modelo.enums.TipoPasajero;
import destinosonado.servicios.CalculadoraPrecio;

/* Comprueba que los datos se conservan al cerrar y volver a abrir el archivo. */
public class RepositorioArchivoTest {

    public static void main(String[] args) throws Exception {
        File archivo = new File(System.getProperty("java.io.tmpdir"), "agencia-test-" + System.nanoTime() + ".dat");
        try {
            // Primera "ejecucion": se crea una salida con una reserva y se paga la senia
            AlmacenArchivo almacen1 = new AlmacenArchivo(archivo.getPath());
            RepositorioArchivo<Salida> salidas1 = new RepositorioArchivo<>(almacen1, "salidas", Salida::getId);
            RepositorioArchivo<Reserva> reservas1 = new RepositorioArchivo<>(almacen1, "reservas", Reserva::getId);
            salidas1.guardar(new Salida(1, new TourDePrueba(2, 4, 1000, false), LocalDate.now().plusDays(40), null, null));
            ControladorReservas controlador = new ControladorReservas(salidas1, reservas1, new CalculadoraPrecio());

            List<DatosPasajero> pasajeros = new ArrayList<>();
            pasajeros.add(new DatosPasajero("Ana", "Lopez", "30111222", "11-1111-1111",
                    LocalDate.of(1990, 1, 1), TipoPasajero.ADULTO, "11-2222-2222"));
            Reserva creada = controlador.crearReserva(1, null, null, pasajeros);
            controlador.registrarPago(creada.getId(), new PagoEfectivo(new Date(), 400));

            // Segunda "ejecucion": se abre el mismo archivo con repositorios nuevos
            AlmacenArchivo almacen2 = new AlmacenArchivo(archivo.getPath());
            RepositorioArchivo<Salida> salidas2 = new RepositorioArchivo<>(almacen2, "salidas", Salida::getId);
            RepositorioArchivo<Reserva> reservas2 = new RepositorioArchivo<>(almacen2, "reservas", Reserva::getId);

            Verificador.igual("la salida se conserva al reabrir el archivo", 1, salidas2.listarTodos().size());
            Verificador.igual("la reserva se conserva al reabrir el archivo", 1, reservas2.listarTodos().size());

            Reserva leida = reservas2.buscarPorId(creada.getId());
            Verificador.igual("el total de la reserva se conserva", 1000, leida.getTotal());
            Verificador.igual("el pago registrado se conserva", 400, leida.montoPagado());
            Verificador.igualTexto("el estado CONFIRMADA se conserva", EstadoReserva.CONFIRMADA, leida.getEstado());
            Verificador.igualTexto("el pasajero se conserva", "Ana", leida.getPasajeros().get(0).getNombre());
            Verificador.verdadero("la reserva y su salida siguen siendo el mismo objeto (no copias)",
                    leida.getSalida() == salidas2.buscarPorId(1));
            Verificador.verdadero("buscar un id que no existe devuelve null", salidas2.buscarPorId(99) == null);
        } finally {
            archivo.delete(); // se borra el archivo de prueba, pase lo que pase
        }
        Verificador.resumen();
    }
}
