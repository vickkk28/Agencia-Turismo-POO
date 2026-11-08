package destinosonado;

import java.time.LocalTime;

public class Actividad {
    private int dia;
    private int orden;
    private LocalTime horario;
    private String descripcion;
    private String lugar;

    private Proveedor proveedor;

    public Actividad(int dia, int orden, LocalTime horario,
                     String descripcion, String lugar , Proveedor proveedor) {
        this.dia = dia;
        this.orden = orden;
        this.horario = horario;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.proveedor=proveedor;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

}
