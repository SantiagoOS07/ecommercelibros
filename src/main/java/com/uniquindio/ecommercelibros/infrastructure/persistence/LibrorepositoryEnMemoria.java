package com.uniquindio.ecommercelibros.infrastructure.persistence;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LibrorepositoryEnMemoria implements LibroRepository {

    private final Map<String, Libro> libros = new HashMap<>();


    @Override
    public Optional<Libro> buscarPorISBN(String ISBN) {
        return Optional.ofNullable(libros.get(ISBN));
    }

    @Override
    public Optional<Libro> buscarPorIdLibro(UUID idLibro) {
        return Optional.ofNullable(libros.get(idLibro.toString()));
    }

    @Override
    public Optional<Libro> buscarPorNombre(String nombre) {
        return Optional.ofNullable(libros.get(nombre));
    }

    @Override
    public Optional<Libro> buscarPorAutor(String nombreAutor) {
        return Optional.ofNullable(libros.get(nombreAutor));
    }

    @Override
    public void guardarLibro(Libro libro) {
        libros.put(libro.getIdLibro().toString(), libro);
    }
}
