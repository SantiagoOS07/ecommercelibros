package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

public class VerificadorDeLibro {

    private final LibroRepository libroRepository;

    public VerificadorDeLibro(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void verificarQueISBNNoExiste(String isbn) {

        boolean existeLibro = libroRepository.existePorIsbn(isbn);

        if (existeLibro) {
            throw new RecursoDuplicadoException(
                    "Ya existe un libro registrado con ese ISBN"
            );
        }
    }
}
