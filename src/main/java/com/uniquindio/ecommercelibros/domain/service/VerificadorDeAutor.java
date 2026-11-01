package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
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
        if (autorRepository.existePorId(idAutor)) {
            throw new RecursoDuplicadoException("Ya existe un autor registrado con ese id");
        }
    }

    
    public void verificarQueAutorExiste(String idAutor) {
        if (autorRepository.buscarPorIdAutor(idAutor).isEmpty()) {
            throw new RecursoNoEncontradoException("No existe un autor con el id: " + idAutor);
        }
    }

    
    public void verificarQueNoTieneLibros(String idAutor) {
        if (!libroRepository.buscarPorAutor(idAutor).isEmpty()) {
            throw new ReglaDominioException("No se puede eliminar un autor que tiene libros registrados");
        }
    }
}