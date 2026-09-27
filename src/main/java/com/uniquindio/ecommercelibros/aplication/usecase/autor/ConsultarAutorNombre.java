package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;

import java.util.Optional;

public class ConsultarAutorNombre {

    private final AutorRepository autorRepository;

    public ConsultarAutorNombre(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Optional<Autor> buscar(String nombreAutor) {
        return autorRepository.buscarPorNombre(nombreAutor);
    }
}
