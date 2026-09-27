package com.uniquindio.ecommercelibros.infrastructure.persistence;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AutorRepositoryEnMemoria implements AutorRepository {

    private final Map<String, Autor> autores = new HashMap<>();

    @Override
    public Optional<Autor> buscarPorIdAutor(String idAutor) {
        return Optional.ofNullable(autores.get(idAutor));
    }

    @Override
    public Optional<Autor> buscarPorNombre(String nombreAutor) {
        return autores.values()
                .stream()
                .filter(autor -> autor.getNombreAutor().equalsIgnoreCase(nombreAutor))
                .findFirst();
    }

    @Override
    public List<Autor> listarTodos() {
        return List.copyOf(autores.values());
    }

    @Override
    public void guardarAutor(Autor autor) {
        autores.put(autor.getIdAutor(), autor);
    }

    @Override
    public void eliminarAutor(String idAutor) {
        autores.remove(idAutor);
    }
}
