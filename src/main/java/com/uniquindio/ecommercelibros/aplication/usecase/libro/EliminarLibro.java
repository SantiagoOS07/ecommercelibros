package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EliminarLibro {

    private final LibroRepository libroRepository;

    public EliminarLibro(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional
    public void eliminar(UUID idLibro) {

        libroRepository.buscarPorIdLibro(idLibro)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El libro no existe"));

        libroRepository.eliminarLibro(idLibro);
    }
}