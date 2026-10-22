package com.uniquindio.ecommercelibros.aplication.dto.response.carrito;

import java.math.BigDecimal;
import java.util.UUID;

/** Representación de un artículo dentro del carrito */
public record ItemCarritoResponse(
        UUID idItemCarrito,
        UUID idLibro,
        String tituloLibro,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}
