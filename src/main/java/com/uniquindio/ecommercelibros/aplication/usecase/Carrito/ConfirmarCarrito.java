package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConfirmarCarrito {

    private final CarritoRepository carritoRepository;

    public ConfirmarCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Transactional
    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository.buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El carrito no existe."));

        carrito.confirmarCarrito();
        carritoRepository.guardarCarrito(carrito);
    }
}

