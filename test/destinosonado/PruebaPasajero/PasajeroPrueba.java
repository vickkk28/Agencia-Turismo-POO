package destinosonado.PruebaPasajero;
import java.util.ArrayList;
import java.util.List;
import destinosonado.*;

import java.time.LocalDate;

class Main {
    public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBA DE LA AGENCIA ---");

        // 1. Creamos un pasajero
        List<ServicioAdicional> misServicios = new ArrayList<>();
        LocalDate fechaNac = LocalDate.of(2000, 5, 10);
        Pasajero pasajero1 = new Pasajero("Carlos", "Gomez", "35123456", "1155443322", fechaNac, "Ninguna", misServicios);

        // Probamos el cálculo de edad con una fecha específica
        LocalDate fechaActual = LocalDate.of(2026, 11, 2);
        int edad = pasajero1.CalcularEdad(fechaActual);
        System.out.println("Pasajero: " + pasajero1.getNombreCompleto() + " - Edad: " + edad);

        // 2. Creamos los colaboradores necesarios para la reserva
        Cliente titular = new Cliente("Maria", "Lopez", "25111222", "1144556677", "maria@email.com", "Argentina");
        Vendedor vendedor = new Vendedor("Juan", "Perez", "20123456", "1177889900", 101, 4.5);

        // Creamos una salida
        LocalDate inicioSalida = LocalDate.of(2026, 12, 15);
        LocalDate finSalida = LocalDate.of(2026, 12, 22);
        Salida salida = new Salida(inicioSalida, finSalida, 5, 30, null);

        // 3. Creamos la Reserva y le agregamos el pasajero
        Reserva reserva = new Reserva(100, LocalDate.now(), null, 50000.0, titular, vendedor, salida);
        reserva.agregarPasajero(pasajero1);

        // 4. Probamos los métodos de la reserva
        System.out.println("Cantidad de pasajeros en la reserva: " + reserva.cantidadPasajeros());
        System.out.println("Fecha de salida de la reserva: " + reserva.fechaSalida());
        //System.out.println("Saldo pendiente de la reserva: " + reserva.saldo());

        System.out.println("--- FIN DE LA PRUEBA ---");
    }
}