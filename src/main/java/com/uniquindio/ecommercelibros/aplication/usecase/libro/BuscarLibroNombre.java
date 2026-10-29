package com.uniquindio.ecommercelibros.aplication.usecase.libro;


import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuscarLibroNombre {

    private final LibroRepository libroRepository;

    public BuscarLibroNombre(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public List<Libro> buscar(String nombre) {
        return libroRepository.buscarPorNombre(nombre);
    }
}
