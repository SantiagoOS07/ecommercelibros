package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.Optional;
import java.util.UUID;

public class ConsultarLibroID {

    private final LibroRepository libroRepository;

    public ConsultarLibroID(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Optional<Libro> ConsultarLibroPorID(UUID id){
        return libroRepository.buscarPorIdLibro(id);
    }
}
