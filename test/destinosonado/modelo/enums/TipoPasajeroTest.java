package destinosonado.modelo.enums;

import destinosonado.Verificador;

public class TipoPasajeroTest {

    public static void main(String[] args) {
        Verificador.igual("adulto paga tarifa completa", 1.0, TipoPasajero.ADULTO.factorTarifa());
        Verificador.igual("menor paga el 60%", 0.6, TipoPasajero.MENOR.factorTarifa());
        Verificador.igual("infante no paga", 0.0, TipoPasajero.INFANTE.factorTarifa());
        Verificador.igual("jubilado tiene 10% de descuento", 0.9, TipoPasajero.JUBILADO.factorTarifa());
        Verificador.resumen();
    }
}