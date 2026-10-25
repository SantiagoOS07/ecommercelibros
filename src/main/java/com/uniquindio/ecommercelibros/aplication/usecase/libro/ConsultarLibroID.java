package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultarLibroID {

    private final LibroRepository libroRepository;

    public ConsultarLibroID(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Libro> ConsultarLibroPorID(UUID id){
        return libroRepository.buscarPorIdLibro(id);
    }
}
