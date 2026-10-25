package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
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

