package com.uniquindio.ecommercelibros.aplication.usecase.libro;


import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.Optional;

public class BuscarLibroNombre {

    private final LibroRepository libroRepository;

    public BuscarLibroNombre(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Optional<Libro> buscar(String nombre) {
        return libroRepository.buscarPorNombre(nombre);
    }
}
