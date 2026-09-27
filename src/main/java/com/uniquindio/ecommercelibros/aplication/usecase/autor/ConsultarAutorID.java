package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;

import java.util.Optional;

public class ConsultarAutorID {

    private final AutorRepository autorRepository;

    public ConsultarAutorID(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Optional<Autor> consultar(String idAutor) {
        return autorRepository.buscarPorIdAutor(idAutor);
    }
}
