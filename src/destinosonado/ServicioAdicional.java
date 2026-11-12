package destinosonado;

public class ServicioAdicional implements java.io.Serializable {
    private String nombre;
    private double precio;
    private boolean obligatorio;

    //Constructor
    public ServicioAdicional(String nombre, double precio, boolean obligatorio) {
        this.nombre = nombre;
        this.precio = precio;
        this.obligatorio = obligatorio;
    }

    //Métodos
    public boolean esSeguro() {
        if (this.nombre != null) {
            String nombreMinuscula = this.nombre.toLowerCase();
            return nombreMinuscula.contains("seguro") || nombreMinuscula.contains("asistencia");
        }
        return false;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    //Setters
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }
}


