package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarLibros {

    private static final int TAMANIO_MAXIMO = 20;

    private final LibroRepository libroRepository;

    @Transactional(readOnly = true)
    public Pagina<Libro> ejecutar(String palabraTitulo, EstadoLibro estado, int pagina, int tamanio) {
        if (pagina < 0) {
            throw new ReglaDominioException("La página no puede ser negativa");
        }
        if (tamanio < 1 || tamanio > TAMANIO_MAXIMO){
            throw new ReglaDominioException("El tamaño de la pagina debe estar entre 1 y " + TAMANIO_MAXIMO);
        }
        return libroRepository.buscarLibros(palabraTitulo, estado, pagina, tamanio);
    }
}