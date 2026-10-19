package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;

import java.util.UUID;

public class ConfirmarCarrito {

    private final CarritoRepository carritoRepository;

    public ConfirmarCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository.buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException("El carrito no existe."));

        carrito.confirmarCarrito();

        carritoRepository.guardarCarrito(carrito);
    }
}

