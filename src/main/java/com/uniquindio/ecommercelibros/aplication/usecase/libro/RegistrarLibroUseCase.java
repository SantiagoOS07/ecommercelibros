package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.RegistrarLibroRequest;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RegistrarLibroUseCase {

    private final LibroRepository libroRepository;
    private final VerificadorDeLibro verificadorDeLibro;

    public RegistrarLibroUseCase(LibroRepository libroRepository, VerificadorDeLibro verificadorDeLibro) {
        this.libroRepository = libroRepository;
        this.verificadorDeLibro = verificadorDeLibro;
    }

    @Transactional
    public Libro registrarLibro(Libro libro) {
        verificadorDeLibro.verificarQueISBNNoExiste(libro.getIsbn().isbn());
        libroRepository.guardarLibro(libro);
        return libro;
    }
}
