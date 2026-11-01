package com.uniquindio.ecommercelibros.aplication.usecase.editorial;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConsultarEditorialID {

    private final EditorialRepository editorialRepository;

    public ConsultarEditorialID(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    @Transactional(readOnly = true)
    public Editorial consultar(UUID idEditorial) {
        return editorialRepository.buscarPorId(idEditorial)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la editorial con ID: " + idEditorial));
    }
}