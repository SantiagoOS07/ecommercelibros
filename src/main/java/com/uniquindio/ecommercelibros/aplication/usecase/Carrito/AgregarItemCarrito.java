
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AgregarItemCarrito {

    private final CarritoRepository carritoRepository;

    public AgregarItemCarrito(
            CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Transactional
    public void ejecutar(
            UUID idCarrito,
            ISBN isbnLibro,
            int cantidad,
            Precio precioLibro,
            Stock stockLibro) {

        Carrito carrito = carritoRepository
                .buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito no existe."
                        ));

        carrito.agregarItem(
                isbnLibro,
                cantidad,
                precioLibro,
                stockLibro
        );

        carritoRepository.guardarCarrito(carrito);
    }
}
