package destinosonado.controlador;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import destinosonado.Persistencia.Repositorio;

/* Repositorio que guarda en memoria, para probar los controladores sin archivos. */
public class RepositorioEnMemoria<T> implements Repositorio<T> {

    private final Map<Integer, T> datos = new LinkedHashMap<>();
    private final ToIntFunction<T> idDe;

    public RepositorioEnMemoria(ToIntFunction<T> idDe) {
        this.idDe = idDe;
    }

    @Override
    public void guardar(T objeto) {
        datos.put(idDe.applyAsInt(objeto), objeto);
    }

    @Override
    public T buscarPorId(int id) {
        return datos.get(id);
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(datos.values());
    }
}
