package destinosonado.Persistencia;

import java.util.List;

/**
 * Contrato para guardar y recuperar objetos. Los controladores dependen de esta
 * interfaz y no de como se guardan los datos (Inversion de Dependencias).
 * RepositorioArchivo (guarda en un archivo) es una de sus implementaciones.
 */
public interface Repositorio<T> {

    void guardar(T objeto);

    T buscarPorId(int id);

    List<T> listarTodos();
}
