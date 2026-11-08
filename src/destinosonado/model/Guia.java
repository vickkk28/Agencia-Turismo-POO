package destinosonado.model;

import destinosonado.Empleados.Empleado;
import destinosonado.modelo.enums.Categoria;

import java.util.List;

public class Guia extends Empleado {
    private List<String> idiomas;
    private List<Categoria> categoriasHabilitadas;
    private double honorarioPorDia;

    //Constructor
    public Guia(String nombre, String apellido, String documento, String telefono, int legajo ,  List<String> idiomas , List<Categoria> categoriasHabilitadas , double honorarioPorDia) {
        super(nombre, apellido, documento, telefono, legajo);
        this.idiomas = idiomas;
        this.categoriasHabilitadas = categoriasHabilitadas;
        this.honorarioPorDia = honorarioPorDia;
    }

    public boolean estaHabilitado(Categoria c){
        return this.categoriasHabilitadas.contains(c);
    }

    //Rol
    @Override
    public String descripcionRol() {
        return "Guía de turismo"; // o "Guía"
    }

    //Obtenciones
    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public List<Categoria> getCategoriasHabilitadas() {
        return categoriasHabilitadas;
    }

    public void setCategoriasHabilitadas(List<Categoria> categoriasHabilitadas) {
        this.categoriasHabilitadas = categoriasHabilitadas;
    }

    public double getHonorarioPorDia() {
        return honorarioPorDia;
    }

    public void setHonorarioPorDia(double honorarioPorDia) {
        this.honorarioPorDia = honorarioPorDia;
    }
}
