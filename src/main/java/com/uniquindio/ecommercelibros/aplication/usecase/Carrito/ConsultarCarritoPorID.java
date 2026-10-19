package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;


import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;

import java.util.Optional;
import java.util.UUID;

public class ConsultarCarritoPorID {

    private final CarritoRepository carritoRepository;

    public ConsultarCarritoPorID(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public Optional<Carrito> ejecutar(UUID idCarrito) {
        return carritoRepository.buscarPorIdCarrito(idCarrito);
    }
}

