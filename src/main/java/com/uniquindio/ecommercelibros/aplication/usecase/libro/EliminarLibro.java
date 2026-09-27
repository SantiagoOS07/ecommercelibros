package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.UUID;

public class EliminarLibro {

    private final LibroRepository libroRepository;

    public EliminarLibro(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void eliminar(UUID idLibro) {

        libroRepository.buscarPorIdLibro(idLibro)
                .orElseThrow(() ->
                        new IllegalArgumentException("El libro no existe"));

        libroRepository.eliminarLibro(idLibro);
    }
}