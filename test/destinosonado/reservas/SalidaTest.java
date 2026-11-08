package destinosonado.reservas;

import java.time.LocalDate;

import destinosonado.model.Salida;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.excepciones.CupoInsuficienteException;
import destinosonado.modelo.enums.EstadoSalida;

public class SalidaTest {

    public static void main(String[] args) throws Exception {
        Salida salida = new Salida(1, new TourDePrueba(2, 4, 1000, false),
                LocalDate.of(2027, 1, 10), null, null);

        Verificador.igual("Una salida nueva tiene todo el cupo disponible", 4, salida.lugaresDisponibles());
        Verificador.verdadero("Una salida programada admite reservas", salida.admiteReservas());
        Verificador.igualTexto("La fecha de fin surge de la duracion del tour (3 dias)",
                LocalDate.of(2027, 1, 12), salida.getFechaFin());

        salida.ocupar(3);
        Verificador.igual("Al ocupar 3 lugares quedan 1", 1, salida.lugaresDisponibles());

        Verificador.lanza("Pedir mas lugares de los disponibles lanza CupoInsuficiente",
                CupoInsuficienteException.class, () -> salida.ocupar(2));

        salida.ocupar(1);
        Verificador.igualTexto("Al llegar al cupo maximo pasa a COMPLETA",
                EstadoSalida.COMPLETA, salida.getEstado());
        Verificador.verdadero("Una salida completa no admite reservas", !salida.admiteReservas());

        salida.liberar(2);
        Verificador.verdadero("Al liberar lugares vuelve a admitir reservas", salida.admiteReservas());
        Verificador.igual("quedan 2 lugares disponibles", 2, salida.lugaresDisponibles());

        Verificador.resumen();
    }
}