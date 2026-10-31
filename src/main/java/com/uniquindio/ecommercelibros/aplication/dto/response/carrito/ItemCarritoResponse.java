package com.uniquindio.ecommercelibros.aplication.dto.response.carrito;

import java.util.UUID;

/** Representación de un artículo dentro del carrito */
public record ItemCarritoResponse(
        UUID idItemCarrito,
        String isbnLibro,
        String tituloLibro,
        int cantidad,
        double precioUnitario,
        double precioTotal
) {}