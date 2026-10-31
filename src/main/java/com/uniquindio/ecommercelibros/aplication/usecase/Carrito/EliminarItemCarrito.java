package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EliminarItemCarrito {

    private final CarritoRepository carritoRepository;
    private final LibroRepository libroRepository;

    public EliminarItemCarrito(CarritoRepository carritoRepository,
                               LibroRepository libroRepository) {
        this.carritoRepository = carritoRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public void ejecutar(UUID idCarrito, UUID idLibro) {

        Carrito carrito = carritoRepository.buscarPorIdCarrito(idCarrito)
                .orElseThrow(() -> new RecursoNoEncontradoException("El carrito no existe."));

        Libro libro = libroRepository.buscarPorIdLibro(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con ID: " + idLibro));

        if (!carrito.contieneLibro(libro.getIsbn())) {
            throw new RecursoNoEncontradoException("El carrito no contiene este libro");
        }
        carrito.eliminarItem(libro.getIsbn());

        carritoRepository.guardarCarrito(carrito);
    }
}