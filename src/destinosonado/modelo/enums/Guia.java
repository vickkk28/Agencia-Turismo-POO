package destinosonado.modelo.enums;

import destinosonado.Empleado;

import java.util.List;

public class Guia extends Empleado {
    private List<String> idiomas;
    private List<String> categoriasHabilitadas; // O una lista de Enums de categorías
    private double honorarioPorDia;

    public Guia(String nombre, String apellido, String documento, String telefono, int legajo ,  List<String> idiomas , List<String> categoriasHabilitadas , double honorarioPorDia) {
        super(nombre, apellido, documento, telefono, legajo);
        this.idiomas = idiomas;
        this.categoriasHabilitadas = categoriasHabilitadas;
        this.honorarioPorDia = honorarioPorDia;
    }

    public boolean estaHabilitado(Categoria c){
        return this.categoriasHabilitadas.contains(c);
    }

    @Override
    public String descripcionRol() {
        return "Guía de turismo"; // o "Guía"
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public List<String> getCategoriasHabilitadas() {
        return categoriasHabilitadas;
    }

    public void setCategoriasHabilitadas(List<String> categoriasHabilitadas) {
        this.categoriasHabilitadas = categoriasHabilitadas;
    }

    public double getHonorarioPorDia() {
        return honorarioPorDia;
    }

    public void setHonorarioPorDia(double honorarioPorDia) {
        this.honorarioPorDia = honorarioPorDia;
    }
}
