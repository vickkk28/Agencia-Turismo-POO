package destinosonado;

import java.time.LocalTime;

public class Actividad {
    private int dia;
    private int orden;
    private LocalTime horario;
    private String descripcion;
    private String lugar;

    public Actividad(int dia, int orden, LocalTime horario,
                     String descripcion, String lugar) {
        this.dia = dia;
        this.orden = orden;
        this.horario = horario;
        this.descripcion = descripcion;
        this.lugar = lugar;
    }

}
