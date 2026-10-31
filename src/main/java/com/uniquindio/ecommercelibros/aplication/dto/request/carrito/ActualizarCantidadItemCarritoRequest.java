package com.uniquindio.ecommercelibros.aplication.dto.request.carrito;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** PUT /carritos/{idCarrito}/items/{idLibro} */
public record ActualizarCantidadItemCarritoRequest(

        @Schema(description = "Nueva cantidad del libro en el carrito (0 elimina el ítem)", example = "3")
        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}