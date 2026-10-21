package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

public class VerificadorDeLibro {

    private final LibroRepository libroRepository;

    public VerificadorDeLibro(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void verificarQueISBNNoExiste(String isbn) {

        boolean existeLibro = libroRepository
                .buscarPorISBN(isbn)
                .isPresent();

        if (existeLibro) {
            throw new ReglaDominioException(
                    "Ya existe un libro registrado con ese ISBN"
            );
        }
    }
}
