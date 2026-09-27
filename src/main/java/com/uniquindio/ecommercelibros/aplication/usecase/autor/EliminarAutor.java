package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;

public class EliminarAutor {

    private final AutorRepository autorRepository;

    public EliminarAutor(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void eliminar(String idAutor) {

        autorRepository.buscarPorIdAutor(idAutor)
                .orElseThrow(() ->
                        new IllegalArgumentException("El autor no existe"));

        autorRepository.eliminarAutor(idAutor);
    }
}
