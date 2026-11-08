package destinosonado.GuiaPrueba;

import destinosonado.model.Guia;
import destinosonado.modelo.enums.Categoria;

import java.util.ArrayList;
import java.util.List;

public class PruebaGuia {
    public static void main(String[] args) {
        List<String> misIdiomas = new ArrayList<>();
        misIdiomas.add("Español");
        misIdiomas.add("Inglés");

        List<Categoria> misCategorias = new ArrayList<>();
        misCategorias.add(Categoria.AVENTURA);
        misCategorias.add(Categoria.CULTURAL);

        Guia guia = new Guia("Carlos", "López", "33444555", "11445566", 12345,
                misIdiomas, misCategorias, 50000.0);

        if (guia.estaHabilitado(Categoria.AVENTURA)) {
            System.out.println("prueba exitosa: El guía está habilitado para Aventura.");
        } else {
            System.out.println("prueba fallida: Debería estar habilitado.");
        }

        if (!guia.estaHabilitado(Categoria.NATURALEZA)) {
            System.out.println("prueba exitosa: El guía NO está habilitado para Naturaleza.");
        } else {
            System.out.println("prueba fallida: No debería estar Naturaleza.");
        }
    }
}