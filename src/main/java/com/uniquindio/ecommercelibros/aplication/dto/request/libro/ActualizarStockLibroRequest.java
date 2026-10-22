package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** PUT /libros/{idLibro}/stock */
public record ActualizarStockLibroRequest(
        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock
) {}
