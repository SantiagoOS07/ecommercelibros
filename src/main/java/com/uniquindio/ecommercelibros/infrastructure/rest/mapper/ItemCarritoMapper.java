package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.AgregarItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.ItemCarritoResponse;
import com.uniquindio.ecommercelibros.domain.entity.ItemCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import org.springframework.stereotype.Component;

@Component
public class ItemCarritoMapper {

    public ItemCarritoResponse toResponse(
            ItemCarrito itemCarrito,
            String tituloLibro) {

        return new ItemCarritoResponse(
                itemCarrito.getIdItemCarrito(),
                itemCarrito.getIsbnLibro(),
                tituloLibro,
                itemCarrito.getCantidad(),
                itemCarrito.getPrecioUnitario().monto(),
                itemCarrito.getPrecioTotal().monto()
        );
    }


    public ItemCarrito toDomain(
            AgregarItemCarritoRequest request,
            Libro libro) {

        return ItemCarrito.crear(
                libro.getIsbn(),
                request.cantidad(),
                libro.getPrecio(),
                libro.getStock()
        );
    }

}
