package destinosonado.controlador;

import destinosonado.*;
import destinosonado.model.Guia;
import destinosonado.Persistencia.Repositorio;
import destinosonado.excepciones.ReglaDeNegocioException;
import destinosonado.model.Pasajero;
import destinosonado.model.Reserva;
import destinosonado.model.Salida;
import destinosonado.model.Tour;
import destinosonado.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControladorSalidas {
    private Repositorio<Tour> repoTours;
    private Repositorio<Salida> repoSalidas;
    private LiquidadorHonorarios liquidador;

    public ControladorSalidas(Repositorio<Tour> repoTours, Repositorio<Salida> repoSalidas, LiquidadorHonorarios liquidador) {
        this.repoTours = repoTours;
        this.repoSalidas = repoSalidas;
        this.liquidador = liquidador;
    }

    public Salida programarSalida(int idTour, LocalDate inicio, Guia guia, Vehiculo vehiculo) throws ReglaDeNegocioException {
        Tour tour = repoTours.buscarPorId(idTour);
        if (tour == null) {
            throw new ReglaDeNegocioException("No existe el Tour con ID: " + idTour);
        }
        validarProgramacion(tour, inicio, guia, vehiculo);
        // Genera un ID autoincremental básico basado en el total de salidas registradas
        int nuevoId = repoSalidas.listarTodos().size() + 1;

        Salida nuevaSalida = new Salida(nuevoId, tour, inicio, guia, vehiculo);
        repoSalidas.guardar(nuevaSalida);
        return nuevaSalida;

    }

    /** Reglas para programar una salida: fecha futura, guia habilitado y libre, vehiculo con capacidad. */
    private void validarProgramacion(Tour tour, LocalDate inicio, Guia guia, Vehiculo vehiculo)
            throws ReglaDeNegocioException {
        if (inicio == null || inicio.isBefore(LocalDate.now())) {
            throw new ReglaDeNegocioException("La fecha de inicio no puede ser anterior a hoy");
        }
        if (guia != null && !guia.estaHabilitado(tour.getCategoria())) {
            throw new ReglaDeNegocioException("El guia " + guia.getNombreCompleto()
                    + " no esta habilitado para tours de categoria " + tour.getCategoria());
        }
        if (vehiculo != null && vehiculo.getCapacidad() < tour.getCupoMaximo()) {
            throw new ReglaDeNegocioException("El vehiculo " + vehiculo.getPatente() + " tiene capacidad para "
                    + vehiculo.getCapacidad() + " y el tour admite hasta " + tour.getCupoMaximo() + " pasajeros");
        }
        LocalDate fin = inicio.plusDays(tour.getDuracionDias() - 1L);
        for (Salida otra : repoSalidas.listarTodos()) {
            boolean mismoGuia = guia != null && otra.getGuia() != null
                    && otra.getGuia().getDocumento().equals(guia.getDocumento());
            boolean seSuperponen = !inicio.isAfter(otra.getFechaFin()) && !fin.isBefore(otra.getFechaInicio());
            if (mismoGuia && seSuperponen) {
                throw new ReglaDeNegocioException("El guia " + guia.getNombreCompleto()
                        + " ya tiene la salida " + otra.getId() + " en esas fechas");
            }
        }
    }

    public List<Pasajero> emitirListaPasajeros(int idSalida) throws ReglaDeNegocioException {
        Salida salida = repoSalidas.buscarPorId(idSalida);
        if (salida == null) {
            throw new ReglaDeNegocioException("No existe la salida con ID: " + idSalida);
        }
        List<Pasajero> listaPasajeros = new ArrayList<>();
        for (Reserva reserva : salida.getReservas()) {
            if (reserva.getEstado() != EstadoReserva.ANULADA) {
                listaPasajeros.addAll(reserva.getPasajeros());
            }
        }
        return listaPasajeros;
    }

    public void verificarSalidas() {
        for (Salida salida : repoSalidas.listarTodos()) {
            salida.confirmarSiCorresponde();
            repoSalidas.guardar(salida); // se guarda el cambio de estado
        }
    }

    public Liquidacion liquidarSalida(int idSalida) throws ReglaDeNegocioException {
        Salida salida = repoSalidas.buscarPorId(idSalida);
        if (salida == null) {
            throw new ReglaDeNegocioException("No existe la salida con ID: " + idSalida);
        }
        return liquidador.liquidar(salida);
    }
    public Repositorio<Tour> getRepoTours() {
        return repoTours;
    }

    public Repositorio<Salida> getRepoSalidas() {
        return repoSalidas;
    }

    public LiquidadorHonorarios getLiquidador() {
        return liquidador;
    }

}
