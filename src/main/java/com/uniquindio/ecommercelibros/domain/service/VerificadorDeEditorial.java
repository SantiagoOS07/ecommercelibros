package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;

import java.util.UUID;

public class VerificadorDeEditorial {

    private final EditorialRepository editorialRepository;

    public VerificadorDeEditorial(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }


    public void verificarQueNombreNoExiste(String nombreEditorial) {
        if (editorialRepository.existePorNombre(nombreEditorial)) {
            throw new RecursoDuplicadoException("Ya existe una editorial registrada con ese nombre");
        }
    }


    public void verificarQueEditorialExiste(UUID idEditorial) {
        if (editorialRepository.buscarPorId(idEditorial).isEmpty()) {
            throw new RecursoNoEncontradoException("No existe una editorial con el id: " + idEditorial);
        }
    }
}