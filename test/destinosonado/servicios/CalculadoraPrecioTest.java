package destinosonado.servicios;

import java.time.LocalDate;
import java.util.List;

import destinosonado.DatosPasajero;
import destinosonado.Reserva;
import destinosonado.Salida;
import destinosonado.ServicioAdicional;
import destinosonado.TourDePrueba;
import destinosonado.Verificador;
import destinosonado.modelo.enums.TipoPasajero;

public class CalculadoraPrecioTest {

    private static final LocalDate SALIDA = LocalDate.of(2027, 3, 1);
    private static final LocalDate RESERVA_ANTICIPADA = LocalDate.of(2026, 12, 1); // 90 dias antes
    private static final LocalDate RESERVA_CERCANA = LocalDate.of(2027, 2, 20);    // 9 dias antes

    private static DatosPasajero pasajero(TipoPasajero tipo) {
        return new DatosPasajero("Pasajero", "Prueba", "30111222", "11-1111-1111",
                LocalDate.of(1990, 1, 1), tipo, "11-5555-0000");
    }

    private static Reserva reserva(LocalDate fechaReserva, TipoPasajero... tipos) throws Exception {
        Salida salida = new Salida(1, new TourDePrueba(2, 10, 1000, false), SALIDA, null, null);
        Reserva reserva = new Reserva(1, fechaReserva, null, null, salida);
        for (TipoPasajero tipo : tipos) {
            reserva.agregarPasajero(pasajero(tipo));
        }
        return reserva;
    }

    public static void main(String[] args) throws Exception {
        CalculadoraPrecio calculadora = new CalculadoraPrecio();

        Verificador.igual("un adulto paga la tarifa completa", 1000,
                calculadora.calcularTotal(reserva(RESERVA_CERCANA, TipoPasajero.ADULTO)));

        Verificador.igual("adulto + menor + infante = 1000 + 600 + 0", 1600,
                calculadora.calcularTotal(reserva(RESERVA_CERCANA,
                        TipoPasajero.ADULTO, TipoPasajero.MENOR, TipoPasajero.INFANTE)));

        Verificador.igual("un jubilado tiene 10 % de descuento", 900,
                calculadora.calcularTotal(reserva(RESERVA_CERCANA, TipoPasajero.JUBILADO)));

        Verificador.igual("reserva anticipada: 15 % de descuento", 850,
                calculadora.calcularTotal(reserva(RESERVA_ANTICIPADA, TipoPasajero.ADULTO)));

        Verificador.igual("grupo de 6: 10 % de descuento", 5400,
                calculadora.calcularTotal(reserva(RESERVA_CERCANA, TipoPasajero.ADULTO, TipoPasajero.ADULTO,
                        TipoPasajero.ADULTO, TipoPasajero.ADULTO, TipoPasajero.ADULTO, TipoPasajero.ADULTO)));

        Verificador.igual("anticipada + grupo: se aplica solo la mas favorable (15 %)", 5100,
                calculadora.calcularTotal(reserva(RESERVA_ANTICIPADA, TipoPasajero.ADULTO, TipoPasajero.ADULTO,
                        TipoPasajero.ADULTO, TipoPasajero.ADULTO, TipoPasajero.ADULTO, TipoPasajero.ADULTO)));

        Salida salida = new Salida(2, new TourDePrueba(2, 10, 1000, false), SALIDA, null, null);
        Reserva conAdicional = new Reserva(2, RESERVA_CERCANA, null, null, salida);
        conAdicional.agregarPasajero(new DatosPasajero("Pasajero", "Prueba", "30111222", "11-1111-1111",
                LocalDate.of(1990, 1, 1), TipoPasajero.ADULTO, "11-5555-0000",
                List.of(new ServicioAdicional("Traslado", 100, false))));
        Verificador.igual("los servicios adicionales se suman al precio", 1100,
                calculadora.calcularTotal(conAdicional));

        Verificador.resumen();
    }
}