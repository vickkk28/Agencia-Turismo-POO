package destinosonado;

public class Persona {
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;

    public String getNombreCompleto() {
        return nombre +" " +  apellido;
    }
}
