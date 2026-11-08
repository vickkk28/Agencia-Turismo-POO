package destinosonado;

public class Proveedor {
    private String nombre;
    private String cuit; // Nota: en el diagrama figura como "cuit"
    private String contacto;
    private String servicio;

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor con parámetros
    public Proveedor(String nombre, String cuit, String contacto, String servicio) {
        this.nombre = nombre;
        this.cuit = cuit;
        this.contacto = contacto;
        this.servicio = servicio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "nombre='" + nombre + '\'' +
                ", cuit='" + cuit + '\'' +
                ", contacto='" + contacto + '\'' +
                ", servicio='" + servicio + '\'' +
                '}';
    }
}

