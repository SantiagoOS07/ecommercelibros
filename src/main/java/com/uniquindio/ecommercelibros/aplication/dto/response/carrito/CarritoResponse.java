package com.uniquindio.ecommercelibros.aplication.dto.response.carrito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** GET /carritos/{idCarrito} */
public record CarritoResponse(
        UUID idCarrito,
        List<ItemCarritoResponse> items,
        BigDecimal precioTotal
) {}
