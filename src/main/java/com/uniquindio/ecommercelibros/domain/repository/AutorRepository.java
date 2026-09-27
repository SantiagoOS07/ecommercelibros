package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Autor;

import java.util.List;
import java.util.Optional;

public interface AutorRepository {

    Optional<Autor> buscarPorIdAutor(String idAutor);

    Optional<Autor> buscarPorNombre(String nombreAutor);

    List<Autor> listarTodos();

    void guardarAutor(Autor autor);

    void eliminarAutor(String idAutor);
}
