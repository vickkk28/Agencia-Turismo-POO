package destinosonado.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import destinosonado.Persistencia.Repositorio;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Tarifa;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.Categoria;
import destinosonado.modelo.enums.Temporada;

/**
 * Controlador del catalogo de tours (patron Controlador). Valida los datos antes de
 * crear el tour; el codigo se asigna solo y es numerico, y hace de id en el repositorio.
 */
public class ControladorTours {

    private final Repositorio<Tour> repoTours;

    public ControladorTours(Repositorio<Tour> repoTours) {
        this.repoTours = repoTours;
    }

    /** Id con el que se guarda un tour en el repositorio (su codigo numerico). */
    public static int idDe(Tour tour) {
        return Integer.parseInt(tour.getCodigo());
    }

    public Tour crearTour(String nombre, String descripcion, int duracionDias, int cupoMinimo,
                          int cupoMaximo, Categoria categoria, double precioBase) throws ReglaDeNegocioException {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaDeNegocioException("El tour necesita un nombre");
        }
        if (categoria == null) {
            throw new ReglaDeNegocioException("El tour necesita una categoria");
        }
        if (duracionDias <= 0) {
            throw new ReglaDeNegocioException("La duracion del tour debe ser de al menos 1 dia");
        }
        if (cupoMinimo <= 0 || cupoMinimo > cupoMaximo) {
            throw new ReglaDeNegocioException("El cupo minimo debe ser mayor a 0 y no superar al cupo maximo");
        }
        if (precioBase <= 0) {
            throw new ReglaDeNegocioException("El precio base debe ser mayor a 0");
        }

        LocalDate hoy = LocalDate.now();
        List<Tarifa> tarifas = new ArrayList<>();
        tarifas.add(new Tarifa(Temporada.MEDIA, hoy.minusYears(1), hoy.plusYears(2), precioBase));

        Tour tour = new Tour(String.valueOf(proximoCodigo()), nombre.trim(), descripcion == null ? "" : descripcion.trim(),
                duracionDias, cupoMinimo, cupoMaximo, new ArrayList<>(), new ArrayList<>(), tarifas, categoria);
        repoTours.guardar(tour);
        return tour;
    }

    public List<Tour> listarTours() {
        return repoTours.listarTodos();
    }

    private int proximoCodigo() {
        int maximo = 0;
        for (Tour t : repoTours.listarTodos()) {
            try {
                maximo = Math.max(maximo, idDe(t));
            } catch (NumberFormatException e) {
                // un codigo no numerico no cuenta para el proximo numero
            }
        }
        return maximo + 1;
    }
}
