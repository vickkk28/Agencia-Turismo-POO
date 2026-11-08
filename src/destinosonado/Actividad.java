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

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "dia=" + dia +
                ", orden=" + orden +
                ", horario=" + horario +
                ", descripcion='" + descripcion + '\'' +
                ", lugar='" + lugar + '\'' +
                ", proveedor=" + proveedor +
                '}';
    }
}
