package destinosonado;

public class ServicioAdicional {

    private String nombre;
    private double precio;
    private boolean obligatorio;

    public ServicioAdicional(String nombre, double precio, boolean obligatorio) {
        this.nombre = nombre;
        this.precio = precio;
        this.obligatorio = obligatorio;
    }

    public boolean esSeguro() {
        if (this.nombre != null) {
            String nombreMinuscula = this.nombre.toLowerCase();
            return nombreMinuscula.contains("seguro") || nombreMinuscula.contains("asistencia");
        }
        return false;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }
}


