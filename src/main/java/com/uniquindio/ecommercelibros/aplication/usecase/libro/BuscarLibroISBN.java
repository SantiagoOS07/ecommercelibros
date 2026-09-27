package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.Optional;

public class BuscarLibroISBN {

    private final LibroRepository libroRepository;

    public BuscarLibroISBN(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Optional<Libro> buscar(String isbn) {
        return libroRepository.buscarPorISBN(isbn);
    }
}