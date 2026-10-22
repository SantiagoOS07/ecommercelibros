
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompletarVentaCarrito {

    private final CarritoRepository carritoRepository;

    public CompletarVentaCarrito(
            CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository
                .buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito no existe."
                        ));

        carrito.completarVentaCarrito();

        carritoRepository.guardarCarrito(carrito);
    }
}
