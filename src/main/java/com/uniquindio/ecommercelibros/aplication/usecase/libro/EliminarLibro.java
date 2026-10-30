package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EliminarLibro {

    private final LibroRepository libroRepository;
    private  final VerificadorDeCarrito verificadorDeCarrito;

    public EliminarLibro(LibroRepository libroRepository, VerificadorDeCarrito verificadorDeCarrito) {
        this.libroRepository = libroRepository;
        this.verificadorDeCarrito = verificadorDeCarrito;
    }

    @Transactional
    public void eliminar(UUID idLibro) {
        Libro libro = libroRepository.buscarPorIdLibro(idLibro)      // ya excluye eliminados -> 404
                .orElseThrow(() -> new RecursoNoEncontradoException("El libro no existe"));
        String isbn = libro.getIsbn().isbn();

        verificadorDeCarrito.verificarQueLibroNoEstaEnCarritoProcesando(isbn);
        libro.eliminarLogicamente();
        libroRepository.guardarLibro(libro);
        verificadorDeCarrito.retirarLibroDeCarritosCreado(isbn);
    }
}