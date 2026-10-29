package destinosonado.modelo.enums;

//Cada tipo de pasajero conoce qué porcentaje de la tarifa base paga.

public enum TipoPasajero {
    ADULTO(1.0),
    MENOR(0.6),      // menor de 12 años: paga el 60%
    INFANTE(0.0),    // menor de 3 años: no paga, pero se registra.
    JUBILADO(0.9);   // 10% de descuento.

    private final double factorTarifa;

    TipoPasajero(double factorTarifa) {
        this.factorTarifa = factorTarifa;
    }

    public double factorTarifa() {
        return factorTarifa;
    }
}