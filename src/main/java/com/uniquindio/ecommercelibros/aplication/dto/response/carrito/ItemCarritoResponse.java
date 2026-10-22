package com.uniquindio.ecommercelibros.aplication.dto.response.carrito;

import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;

import java.math.BigDecimal;
import java.util.UUID;

/** Representación de un artículo dentro del carrito */
public record ItemCarritoResponse(
        UUID idItemCarrito,
        ISBN isbnLibro,
        String tituloLibro,
        int cantidad,
        double precioUnitario,
        double precioTotal
) {}
