package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsultarCarritoPorUsuario {

    private final CarritoRepository carritoRepository;

    public ConsultarCarritoPorUsuario(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public Optional<Carrito> ejecutar(String idUsuario) {
        return carritoRepository.buscarPorIdUsuario(idUsuario);
    }
}

