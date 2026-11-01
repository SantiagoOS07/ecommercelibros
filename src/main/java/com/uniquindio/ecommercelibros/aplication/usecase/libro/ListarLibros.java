package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.FiltroLibros;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarLibros {

    private static final int TAMANIO_MAXIMO = 20;

    private final LibroRepository libroRepository;

    /** Listado paginado con todos los filtros (los nulos se ignoran). */
    @Transactional(readOnly = true)
    public Pagina<Libro> ejecutar(String palabraTitulo, EstadoLibro estado, CategoriaLibro categoria,
                                  Double precioMinimo, Double precioMaximo, Moneda moneda,
                                  int pagina, int tamanio) {
        validarPaginacion(pagina, tamanio);
        FiltroLibros filtro = new FiltroLibros(palabraTitulo, estado, categoria, precioMinimo, precioMaximo, moneda);
        return libroRepository.buscarLibros(filtro, pagina, tamanio);
    }

    /** Firma anterior: solo texto y estado. */
    @Transactional(readOnly = true)
    public Pagina<Libro> ejecutar(String palabraTitulo, EstadoLibro estado, int pagina, int tamanio) {
        return ejecutar(palabraTitulo, estado, null, null, null, null, pagina, tamanio);
    }

    private void validarPaginacion(int pagina, int tamanio) {
        if (pagina < 0) {
            throw new ReglaDominioException("La página no puede ser negativa");
        }
        if (tamanio < 1 || tamanio > TAMANIO_MAXIMO) {
            throw new ReglaDominioException("El tamaño de la pagina debe estar entre 1 y " + TAMANIO_MAXIMO);
        }
    }
}