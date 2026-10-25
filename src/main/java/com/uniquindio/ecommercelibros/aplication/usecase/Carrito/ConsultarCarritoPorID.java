package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;


import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultarCarritoPorID {

    private final CarritoRepository carritoRepository;

    public ConsultarCarritoPorID(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Carrito> ejecutar(UUID idCarrito) {
        return carritoRepository.buscarPorIdCarrito(idCarrito);
    }
}

