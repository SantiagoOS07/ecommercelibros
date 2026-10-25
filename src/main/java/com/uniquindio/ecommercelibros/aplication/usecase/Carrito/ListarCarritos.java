package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarCarritos {

    private final CarritoRepository carritoRepository;

    public ListarCarritos(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public List<Carrito> ejecutar() {
        return carritoRepository.listarTodos();
    }
}

