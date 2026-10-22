
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActualizarCantidadItemCarrito {

    private final CarritoRepository carritoRepository;

    public ActualizarCantidadItemCarrito(
            CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(
            UUID idCarrito,
            ISBN isbnLibro,
            int nuevaCantidad,
            Stock stockLibro) {

        Carrito carrito = carritoRepository
                .buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito no existe."
                        ));

        carrito.actualizarItemCantidad(
                isbnLibro,
                nuevaCantidad,
                stockLibro
        );

        carritoRepository.guardarCarrito(carrito);
    }
}
