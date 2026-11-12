package destinosonado.Persistencia;

import java.util.List;


public interface Repositorio<T> {

    void guardar(T objeto);
    T buscarPorId(int id);
    List<T> listarTodos();
}
