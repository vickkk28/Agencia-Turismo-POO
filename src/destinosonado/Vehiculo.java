package destinosonado;

import destinosonado.modelo.enums.TipoVehiculo;

public class Vehiculo implements java.io.Serializable {

    private String patente;
    private int capacidad;
    private TipoVehiculo tipo;

    public Vehiculo(String patente, int capacidad, TipoVehiculo tipo) {
        this.patente = patente;
        this.capacidad = capacidad;
        this.tipo = tipo;
    }

    public String getPatente() {
        return patente;
    }

    public int getCapacidad() {
        return capacidad;
    }


}
