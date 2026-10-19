package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;

import java.util.UUID;

public class CompletarVentaCarrito {

    private final CarritoRepository carritoRepository;

    public CompletarVentaCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository.buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException("El carrito no existe."));

        carrito.completarVentaCarrito();

        carritoRepository.guardarCarrito(carrito);
    }
}

