package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.ItemCarritoResponse;
import com.uniquindio.ecommercelibros.domain.entity.ItemCarrito;
import org.springframework.stereotype.Component;

@Component
public class ItemCarritoMapper {

    public ItemCarritoResponse toResponse(ItemCarrito itemCarrito, String tituloLibro) {
        return new ItemCarritoResponse(
                itemCarrito.getIdItemCarrito(),
                itemCarrito.getIsbnLibro().isbn(),
                tituloLibro,
                itemCarrito.getCantidad(),
                itemCarrito.getPrecioUnitario().monto(),
                itemCarrito.getPrecioTotal().monto()
        );
    }
}