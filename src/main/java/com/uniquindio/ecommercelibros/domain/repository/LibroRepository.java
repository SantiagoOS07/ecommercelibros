package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Libro;

import java.util.Optional;
import java.util.UUID;

public interface LibroRepository {

    Optional<Libro> buscarPorISBN(String ISBN);
    Optional<Libro> buscarPorIdLibro(UUID idLibro);
    Optional<Libro> buscarPorNombre(String nombre);
    Optional<Libro> buscarPorAutor(String autor);
    void guardarLibro(Libro libro);

}
