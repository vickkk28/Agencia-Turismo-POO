package destinosonado;

import java.time.LocalDate;
import java.util.ArrayList;

import destinosonado.modelo.enums.Categoria;

/**
 * Tour minimo para las pruebas: precio fijo y seguro configurable
 * (un tour de AVENTURA exige seguro), asi las pruebas de reservas
 * no dependen de tarifas ni de itinerarios.
 */
public class TourDePrueba extends Tour {

    private final double precio;

    public TourDePrueba(int cupoMinimo, int cupoMaximo, double precio, boolean exigeSeguro) {
        super("T-PRUEBA", "Tour de prueba", "Tour usado solo en las pruebas", 3,
                cupoMinimo, cupoMaximo, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                exigeSeguro ? Categoria.AVENTURA : Categoria.CULTURAL);
        this.precio = precio;
    }

    @Override
    public double precioBaseFecha(LocalDate fecha) {
        return precio;
    }
}
