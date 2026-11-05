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
}