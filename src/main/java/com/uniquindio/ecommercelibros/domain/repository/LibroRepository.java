package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibroRepository {

    Optional<Libro> buscarPorISBN(String ISBN);
    Optional<Libro> buscarPorIdLibro(UUID idLibro);
    List<Libro> buscarPorNombre(String nombre);
    List<Libro> buscarPorAutor(String autor);
    void guardarLibro(Libro libro);
    boolean existePorIsbn(String isbn);
    Pagina<Libro> buscarLibros(String palabraTitulo, EstadoLibro estado, int pagina, int tamanio);
}
