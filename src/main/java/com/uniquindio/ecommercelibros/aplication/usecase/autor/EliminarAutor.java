package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeAutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EliminarAutor {

    private final AutorRepository autorRepository;
    private final VerificadorDeAutor verificadorDeAutor;

    public EliminarAutor(AutorRepository autorRepository, VerificadorDeAutor verificadorDeAutor) {
        this.autorRepository = autorRepository;
        this.verificadorDeAutor = verificadorDeAutor;
    }

    public void eliminar(String idAutor) {

        autorRepository.buscarPorIdAutor(idAutor)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El autor no existe"));

        verificadorDeAutor.verificarQueNoTieneLibros(idAutor);

        autorRepository.eliminarAutor(idAutor);
    }
}