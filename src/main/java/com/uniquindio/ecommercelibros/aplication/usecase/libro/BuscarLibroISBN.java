package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BuscarLibroISBN {

    private final LibroRepository libroRepository;

    public BuscarLibroISBN(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Libro> buscar(String isbn) {
        return libroRepository.buscarPorISBN(isbn);
    }
}