package destinosonado.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import destinosonado.Persistencia.Repositorio;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.modelo.enums.EstadoReserva;

/**
 * Arma los reportes pedidos por la consigna: ocupacion por salida, ventas por vendedor,
 * ingresos por tour y cancelaciones. Solo lee los datos: no modifica nada.
 * Cada reporte devuelve una lista de lineas de texto lista para mostrar.
 */
public class ServicioReportes {

    private final Repositorio<Salida> repoSalidas;
    private final Repositorio<Reserva> repoReservas;

    public ServicioReportes(Repositorio<Salida> repoSalidas, Repositorio<Reserva> repoReservas) {
        this.repoSalidas = repoSalidas;
        this.repoReservas = repoReservas;
    }

    /** Ocupacion de cada salida, primero las que tienen mas lugares libres (Comparator de Salida). */
    public List<String> ocupacionPorSalida() {
        List<Salida> salidas = new ArrayList<>(repoSalidas.listarTodos());
        salidas.sort(Salida.POR_CUPO_DISPONIBLE);
        List<String> lineas = new ArrayList<>();
        for (Salida s : salidas) {
            int ocupados = s.getCupoMaximo() - s.lugaresDisponibles();
            lineas.add(s.getTour().getNombre() + " (" + s.getFechaInicio() + "): "
                    + ocupados + "/" + s.getCupoMaximo() + " lugares, " + s.getEstado());
        }
        return lineas;
    }

    /** Total vendido por cada vendedor (sin contar reservas anuladas), de mayor a menor. */
    public List<String> ventasPorVendedor() {
        Map<String, Double> ventas = new HashMap<>();
        for (Reserva r : repoReservas.listarTodos()) {
            if (r.getEstado() != EstadoReserva.ANULADA) {
                String vendedor = r.getVendedor() == null ? "Sin vendedor" : r.getVendedor().getNombreCompleto();
                ventas.merge(vendedor, r.getTotal(), Double::sum);
            }
        }
        return ordenarDeMayorAMenor(ventas);
    }

    /** Lo efectivamente cobrado por cada tour (pagado menos devuelto), de mayor a menor. */
    public List<String> ingresosPorTour() {
        Map<String, Double> ingresos = new HashMap<>();
        for (Reserva r : repoReservas.listarTodos()) {
            double devuelto = r.getCancelacion() == null ? 0 : r.getCancelacion().getImporteDevuelto();
            ingresos.merge(r.getSalida().getTour().getNombre(), r.montoPagado() - devuelto, Double::sum);
        }
        return ordenarDeMayorAMenor(ingresos);
    }

    /** Reservas canceladas, con el motivo y el importe devuelto. Se listan ordenadas por Comparable de Reserva. */
    public List<String> cancelaciones() {
        List<Reserva> anuladas = new ArrayList<>();
        for (Reserva r : repoReservas.listarTodos()) {
            if (r.getCancelacion() != null) {
                anuladas.add(r);
            }
        }
        anuladas.sort(null); // orden natural: Reserva es Comparable
        List<String> lineas = new ArrayList<>();
        for (Reserva r : anuladas) {
            lineas.add("Reserva " + r.getNumero() + " - " + r.getCancelacion().getMotivo()
                    + " - devuelto $" + r.getCancelacion().getImporteDevuelto());
        }
        return lineas;
    }

    private List<String> ordenarDeMayorAMenor(Map<String, Double> totales) {
        List<Map.Entry<String, Double>> entradas = new ArrayList<>(totales.entrySet());
        entradas.sort(Map.Entry.<String, Double>comparingByValue().reversed());
        List<String> lineas = new ArrayList<>();
        for (Map.Entry<String, Double> e : entradas) {
            lineas.add(e.getKey() + ": $" + e.getValue());
        }
        return lineas;
    }
}
