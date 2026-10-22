
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrarCarrito {

    private final CarritoRepository carritoRepository;

    public RegistrarCarrito(
            CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public Carrito ejecutar(String idUsuario) {

        Carrito carrito = Carrito.crear(idUsuario);

        carritoRepository.guardarCarrito(carrito);

        return carrito;
    }
}
