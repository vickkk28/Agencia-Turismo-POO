package destinosonado;

public class Cliente extends Persona {
    private String email;
    private String pais;

    public Cliente(String nombre, String apellido, String documento, String telefono , String email , String pais) {
        super(nombre, apellido, documento, telefono);
        this.email = email;
        this.pais = pais ;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
