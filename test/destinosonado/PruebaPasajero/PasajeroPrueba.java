package destinosonado.PruebaPasajero;
import java.util.ArrayList;
import java.util.List;
import destinosonado.*;
import destinosonado.model.Guia;
import destinosonado.Empleados.Vendedor;
import destinosonado.model.*;
import destinosonado.modelo.enums.Categoria;
import destinosonado.modelo.enums.TipoPasajero;
import destinosonado.modelo.enums.TipoVehiculo;

import java.time.LocalDate;

class Main {
    public static void main(String[] args) throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(Categoria.AVENTURA);
        categorias.add(Categoria.CULTURAL);

        // Creamos los idiomas
        List<String> idiomas = new ArrayList<>();
        idiomas.add("Español");
        idiomas.add("Inglés");

        Guia guia = new Guia("Juan", "Perez", "1241423", "213131131", 12345555, idiomas, categorias, 5000.0);

        Vehiculo vehiculo = new Vehiculo(
                "AB123CD",
                40,
                TipoVehiculo.MICRO
        );

        List<Salida> salidas = new ArrayList<>();
        List<Actividad> actividades = new ArrayList<>();
        List<Tarifa> tarifas = new ArrayList<>();

        Tour tour = new Tour("T001", "Aventura en el Norte", "Recorrido turístico por el norte argentino", 7, 5, 30, salidas, actividades, tarifas, Categoria.AVENTURA);

        // 1. La salida se programa desde el tour
        LocalDate inicioSalida = LocalDate.of(2026, 12, 15);
        Salida salida = tour.programarSalida(1, inicioSalida, guia, vehiculo);

        // 2. Creamos los colaboradores necesarios para la reserva
        Cliente titular = new Cliente("Maria", "Lopez", "25111222", "1144556677", "maria@email.com", "Argentina");
        Vendedor vendedor = new Vendedor("Juan", "Perez", "20123456", "1177889900", 101, 4.5);

        // 3. Creamos la Reserva (arranca PENDIENTE, el total lo calcula la CalculadoraPrecio)
        Reserva reserva = new Reserva(100, LocalDate.now(), titular, vendedor, salida);

        // 4. Agregamos un pasajero. Es un tour de aventura: necesita seguro de viaje
        List<ServicioAdicional> misServicios = new ArrayList<>();
        misServicios.add(new ServicioAdicional("Seguro de viaje", 5000.0, true));
        LocalDate fechaNac = LocalDate.of(2000, 5, 10);
        DatosPasajero datos = new DatosPasajero("Carlos", "Gomez", "35123456", "1155443322",
                fechaNac, TipoPasajero.ADULTO, "1100000000", misServicios);
        Pasajero pasajero1 = reserva.agregarPasajero(datos);

        // Probamos el cálculo de edad con una fecha específica
        LocalDate fechaActual = LocalDate.of(2026, 11, 2);
        int edad = pasajero1.CalcularEdad(fechaActual);
        System.out.println("Pasajero: " + pasajero1.getNombreCompleto() + " - Edad: " + edad);

        // 5. Probamos los métodos de la reserva
        System.out.println("Cantidad de pasajeros en la reserva: " + reserva.cantidadPasajeros());
        System.out.println("Fecha de salida de la reserva: " + reserva.fechaSalida());
        System.out.println("Servicios adicionales de la reserva: " + reserva.totalServiciosAdicionales());
    }
}
