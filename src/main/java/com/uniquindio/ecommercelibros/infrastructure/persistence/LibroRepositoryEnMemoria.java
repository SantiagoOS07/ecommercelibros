package com.uniquindio.ecommercelibros.infrastructure.persistence;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.*;
import java.util.stream.Collectors;

public class LibroRepositoryEnMemoria implements LibroRepository {

    private final Map<UUID, Libro> libros = new HashMap<>();


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
    public List<Libro> buscarPorAutor(String idAutor) {
        return libros.values()
                .stream()
                .filter(libro -> libro.getIdAutor().equals(idAutor))
                .toList();
    }

    @Override
    public void guardarLibro(Libro libro) {
        libros.put(libro.getIdLibro(), libro);
    }

    @Override
    public List<Libro> listarTodosLosLibros() {
        return List.copyOf(libros.values());
    }

    @Override
    public void eliminarLibro(UUID idLibro) {
        libros.remove(idLibro);
    }
}
