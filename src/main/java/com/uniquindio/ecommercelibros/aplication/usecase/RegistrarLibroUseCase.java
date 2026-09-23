package com.uniquindio.ecommercelibros.aplication.usecase;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;

import java.util.UUID;

public class RegistrarLibroUseCase {

    private final LibroRepository libroRepository;

    public RegistrarLibroUseCase(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro registrarLibro(NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado) {
        Libro libro = Libro.crear(titulo, idAutor, isbn, descripcion, numeroPaginas, precio, stock, edicion, idEditorial, fechaPublicacion, categoria, imagen, estado);
        libroRepository.guardarLibro(libro);
        return libro;
    }
}
