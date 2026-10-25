package com.uniquindio.ecommercelibros.aplication.usecase.libro;

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

    public Libro registrarLibro(NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado) {
        verificadorDeLibro.verificarQueISBNNoExiste(isbn.isbn());
        Libro libro = Libro.crear(titulo, idAutor, isbn, descripcion, numeroPaginas, precio, stock, edicion, idEditorial, fechaPublicacion, categoria, imagen, estado);
        libroRepository.guardarLibro(libro);
        return libro;
    }
}
