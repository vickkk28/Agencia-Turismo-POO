package destinosonado.gestion;

import java.time.LocalDate;
import java.util.List;

import destinosonado.LiquidadorHonorarios;
import destinosonado.Liquidacion;
import destinosonado.Vehiculo;
import destinosonado.Verificador;
import destinosonado.controlador.ControladorSalidas;
import destinosonado.controlador.ControladorTours;
import destinosonado.controlador.RepositorioEnMemoria;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Guia;
import destinosonado.model.Salida;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.Categoria;
import destinosonado.modelo.enums.TipoVehiculo;

/* Alta de tours y programacion de salidas con sus reglas. */
public class ControladorSalidasToursTest {

    public static void main(String[] args) throws Exception {
        RepositorioEnMemoria<Tour> repoTours = new RepositorioEnMemoria<>(ControladorTours::idDe);
        RepositorioEnMemoria<Salida> repoSalidas = new RepositorioEnMemoria<>(Salida::getId);
        ControladorTours tours = new ControladorTours(repoTours);
        ControladorSalidas salidas = new ControladorSalidas(repoTours, repoSalidas, new LiquidadorHonorarios());

        Tour t1 = tours.crearTour("Iguazu", "3 dias", 3, 2, 20, Categoria.CULTURAL, 100000);
        Tour t2 = tours.crearTour("Fitz Roy", "4 dias", 4, 4, 12, Categoria.AVENTURA, 150000);
        Verificador.igualTexto("primer codigo de tour", "1", t1.getCodigo());
        Verificador.igualTexto("segundo codigo de tour", "2", t2.getCodigo());
        Verificador.igual("cantidad de tours", 2, tours.listarTours().size());
        Verificador.igual("precio base vigente hoy", 100000, t1.precioBaseFecha(LocalDate.now()));
        Verificador.verdadero("tour de aventura exige seguro", t2.requiereSeguro());
        Verificador.lanza("tour sin nombre", ReglaDeNegocioException.class,
                () -> tours.crearTour(" ", "x", 3, 2, 20, Categoria.CULTURAL, 1000));
        Verificador.lanza("cupo minimo mayor al maximo", ReglaDeNegocioException.class,
                () -> tours.crearTour("X", "x", 3, 30, 20, Categoria.CULTURAL, 1000));
        Verificador.lanza("precio cero", ReglaDeNegocioException.class,
                () -> tours.crearTour("X", "x", 3, 2, 20, Categoria.CULTURAL, 0));

        Guia cultural = new Guia("Carlos", "Gomez", "1", "1", 2, List.of("Español"), List.of(Categoria.CULTURAL), 20000);
        Vehiculo micro = new Vehiculo("AB123CD", 20, TipoVehiculo.MICRO);
        Vehiculo chico = new Vehiculo("ZZ999ZZ", 5, TipoVehiculo.CAMIONETA);
        LocalDate hoy = LocalDate.now();

        Salida s = salidas.programarSalida(1, hoy.plusDays(30), cultural, micro);
        Verificador.igual("salida programada con id 1", 1, s.getId());
        Verificador.lanza("fecha en el pasado", ReglaDeNegocioException.class,
                () -> salidas.programarSalida(1, hoy.minusDays(1), cultural, micro));
        Verificador.lanza("guia no habilitado para aventura", ReglaDeNegocioException.class,
                () -> salidas.programarSalida(2, hoy.plusDays(40), cultural, new Vehiculo("QQ111QQ", 12, TipoVehiculo.COMBI)));
        Verificador.lanza("vehiculo sin capacidad suficiente", ReglaDeNegocioException.class,
                () -> salidas.programarSalida(1, hoy.plusDays(90), cultural, chico));
        Verificador.lanza("guia con otra salida superpuesta", ReglaDeNegocioException.class,
                () -> salidas.programarSalida(1, hoy.plusDays(31), cultural, micro));
        Verificador.lanza("tour inexistente", ReglaDeNegocioException.class,
                () -> salidas.programarSalida(99, hoy.plusDays(50), cultural, micro));
        Salida s2 = salidas.programarSalida(1, hoy.plusDays(60), cultural, micro);
        Verificador.igual("segunda salida sin superposicion", 2, s2.getId());

        Liquidacion l = salidas.liquidarSalida(1);
        Verificador.igual("liquidacion: 3 dias x 20000", 60000, l.getMonto());
        Verificador.igual("lista de pasajeros vacia", 0, salidas.emitirListaPasajeros(1).size());

        Verificador.resumen();
    }
}
