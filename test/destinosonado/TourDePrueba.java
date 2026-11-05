package destinosonado;

import java.time.LocalDate;

import destinosonado.modelo.enums.Categoria;
import destinosonado.tours.Tour;

 //Tour minimo para las pruebas: precio fijo y seguro configurable, asi las pruebas de reservas no dependen de tarifas ni itinerarios.
 
public class TourDePrueba extends Tour {

    private final double precio;
    private final boolean exigeSeguro;

    public TourDePrueba(int cupoMinimo, int cupoMaximo, double precio, boolean exigeSeguro) {
        super("T-PRUEBA", "Tour de prueba", 3, cupoMinimo, cupoMaximo);
        this.precio = precio;
        this.exigeSeguro = exigeSeguro;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.CULTURAL;
    }

    @Override
    public boolean requiereSeguro() {
        return exigeSeguro;
    }

    @Override
    public double precioBase(LocalDate fecha) {
        return precio;
    }
}
