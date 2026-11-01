package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeAutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Eliminación lógica (soft delete): el autor queda marcado como eliminado y deja de consultarse,
 * pero su fila se conserva para no romper los libros y ventas históricas que lo referencian.
 */
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
        Autor autor = autorRepository.buscarPorIdAutor(idAutor)    
                .orElseThrow(() -> new RecursoNoEncontradoException("El autor no existe"));

        verificadorDeAutor.verificarQueNoTieneLibros(idAutor);    

        autor.eliminarLogicamente();
        autorRepository.guardarAutor(autor);
    }
}