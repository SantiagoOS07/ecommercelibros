package com.uniquindio.ecommercelibros.aplication.usecase.libro;


import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;

import java.util.List;
import java.util.Optional;

public class BuscarLibroAutor {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public BuscarLibroAutor(
            LibroRepository libroRepository,
            AutorRepository autorRepository) {

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public List<Libro> buscar(String nombreAutor) {

        Optional<Autor> autor = autorRepository.buscarPorNombre(nombreAutor);

        if (autor.isEmpty()) {
            return List.of();
        }

        return libroRepository.buscarPorAutor(autor.get().getIdAutor());
    }
}
