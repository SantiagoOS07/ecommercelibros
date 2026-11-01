package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarLibroAutor {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public BuscarLibroAutor(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    /** GET /api/libros/autor/{nombreAutor}: lista vacía si no hay un autor con ese nombre. */
    @Transactional(readOnly = true)
    public List<Libro> buscar(String nombreAutor) {
        Optional<Autor> autor = autorRepository.buscarPorNombre(nombreAutor);

        if (autor.isEmpty()) {
            return List.of();
        }

        return libroRepository.buscarPorAutor(autor.get().getIdAutor());
    }

    /** GET /api/autores/{idAutor}/libros: 404 si el autor no existe o está eliminado. */
    @Transactional(readOnly = true)
    public List<Libro> buscarPorIdAutor(String idAutor) {
        autorRepository.buscarPorIdAutor(idAutor)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el autor con ID: " + idAutor));

        return libroRepository.buscarPorAutor(idAutor);
    }
}