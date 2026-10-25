package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EliminarItemCarrito {

    private final CarritoRepository carritoRepository;

    public EliminarItemCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void ejecutar(UUID idCarrito, ISBN isbnLibro) {

        Carrito carrito = carritoRepository.buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException("El carrito no existe."));

        if(!carrito.getItems().contains(isbnLibro)) {
            throw new ReglaDominioException("El carrito no contiene este libro");
        }
        carrito.eliminarItem(isbnLibro);

        carritoRepository.guardarCarrito(carrito);
    }
}
