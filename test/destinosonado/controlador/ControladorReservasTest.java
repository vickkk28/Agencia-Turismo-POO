package destinosonado.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import destinosonado.DatosPasajero;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.excepciones.CupoInsuficienteException;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.modelo.enums.EstadoReserva;
import destinosonado.modelo.enums.TipoPasajero;
import destinosonado.servicios.CalculadoraPrecio;

public class ControladorReservasTest {

    private static List<DatosPasajero> adultos(int cantidad) {
        List<DatosPasajero> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            lista.add(new DatosPasajero("Pasajero " + i, "Prueba", "3000000" + i, "11-1111-1111",
                    LocalDate.of(1990, 1, 1), TipoPasajero.ADULTO, "11-5555-0000"));
        }
        return lista;
    }

    public static void main(String[] args) throws Exception {
        RepositorioEnMemoria<Salida> repoSalidas = new RepositorioEnMemoria<>(Salida::getId);
        RepositorioEnMemoria<Reserva> repoReservas = new RepositorioEnMemoria<>(Reserva::getId);
        Salida salida = new Salida(1, new TourDePrueba(2, 4, 1000, false),
                LocalDate.now().plusDays(10), null, null);
        repoSalidas.guardar(salida);
        ControladorReservas controlador = new ControladorReservas(repoSalidas, repoReservas, new CalculadoraPrecio());

        Reserva reserva = controlador.crearReserva(1, null, null, adultos(2));
        Verificador.igualTexto("la reserva creada queda PENDIENTE", EstadoReserva.PENDIENTE, reserva.getEstado());
        Verificador.igual("el total es 2 adultos x 1000", 2000, reserva.getTotal());
        Verificador.igual("la reserva ocupa 2 lugares de la salida", 2, salida.lugaresDisponibles());
        Verificador.igual("la reserva se guarda en el repositorio", 1, repoReservas.listarTodos().size());

        Verificador.lanza("pedir mas lugares de los que quedan lanza CupoInsuficiente",
                CupoInsuficienteException.class, () -> controlador.crearReserva(1, null, null, adultos(3)));

        controlador.crearReserva(1, null, null, adultos(2));
        Verificador.verdadero("al llenar el cupo la salida deja de admitir reservas", !salida.admiteReservas());
        Verificador.lanza("reservar en una salida completa lanza ReglaDeNegocio",
                ReglaDeNegocioException.class, () -> controlador.crearReserva(1, null, null, adultos(1)));

        Verificador.lanza("reservar en una salida inexistente lanza ReglaDeNegocio",
                ReglaDeNegocioException.class, () -> controlador.crearReserva(99, null, null, adultos(1)));

        Verificador.resumen();
    }
}