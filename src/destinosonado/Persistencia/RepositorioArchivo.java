package destinosonado.Persistencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

public class RepositorioArchivo<T> implements Repositorio<T> {

    private final AlmacenArchivo almacen;
    private final String nombre;
    private final ToIntFunction<T> idDe;

    //
    public RepositorioArchivo(AlmacenArchivo almacen, String nombre, ToIntFunction<T> idDe) {
        this.almacen = almacen;
        this.nombre = nombre;
        this.idDe = idDe;
    }

    @Override
    public void guardar(T objeto) {
        almacen.coleccion(nombre).put(idDe.applyAsInt(objeto), objeto);
        almacen.guardar();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T buscarPorId(int id) {
        return (T) almacen.coleccion(nombre).get(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> listarTodos() {
        List<T> lista = new ArrayList<>();
        for (Map.Entry<Integer, Object> entrada : almacen.coleccion(nombre).entrySet()) {
            lista.add((T) entrada.getValue());
        }
        return lista;
    }
}
