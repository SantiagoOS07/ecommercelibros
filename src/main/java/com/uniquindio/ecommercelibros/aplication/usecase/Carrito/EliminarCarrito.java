package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;

import java.util.UUID;

public class EliminarCarrito {

    private final CarritoRepository carritoRepository;

    public EliminarCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(UUID idCarrito) {

        if (carritoRepository.buscarPorIdCarrito(idCarrito).isEmpty()) {
            throw new IllegalArgumentException("El carrito no existe.");
        }

        carritoRepository.eliminarCarrito(idCarrito);
    }
}

