package destinosonado;

import destinosonado.modelo.enums.Temporada;

import java.time.LocalDate;

public class Tarifa {

    private Temporada temporada;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private double precioBase;

    public Tarifa(Temporada temporada, LocalDate vigenciaDesde, LocalDate vigenciaHasta, double precioBase) {
        this.temporada = temporada;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
        this.precioBase = precioBase;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }

    public LocalDate getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(LocalDate vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public LocalDate getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(LocalDate vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }
}