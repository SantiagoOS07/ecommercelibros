package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;

import java.util.UUID;

public class CancelarConfirmacionCarrito {

    private final CarritoRepository carritoRepository;

    public CancelarConfirmacionCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository.buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException("El carrito no existe."));

        carrito.cancelarConfirmacionCarrito();

        carritoRepository.guardarCarrito(carrito);
    }
}

