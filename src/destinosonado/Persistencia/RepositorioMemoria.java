package destinosonado.Persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

/** Guarda todo en memoria mientras el programa está abierto. */
public class RepositorioMemoria<T> implements Repositorio<T> {

    private final Map<Integer, T> datos = new LinkedHashMap<>();
    private final ToIntFunction<T> obtenerId;

    public RepositorioMemoria(ToIntFunction<T> obtenerId) {
        this.obtenerId = obtenerId;
    }

    @Override
    public void guardar(T objeto) {
        datos.put(obtenerId.applyAsInt(objeto), objeto);
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