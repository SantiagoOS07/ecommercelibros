package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

public class VerificadorDeAutor {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public VerificadorDeAutor(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void verificarQueAutorNoExiste(String idAutor) {

        boolean existeAutor = autorRepository
                .buscarPorIdAutor(idAutor)
                .isPresent();

        if (existeAutor) {
            throw new ReglaDominioException(
                    "Ya existe un autor registrado con ese id"
            );
        }
    }

    public void verificarQueNoTieneLibros(String idAutor) {

        boolean tieneLibros = !libroRepository
                .buscarPorAutor(idAutor)
                .isEmpty();

        if (tieneLibros) {
            throw new ReglaDominioException(
                    "No se puede eliminar un autor que tiene libros registrados"
            );
        }
    }
}