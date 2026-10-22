package com.uniquindio.ecommercelibros.aplication.dto.request.carrito;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/** POST /carritos/{idCarrito}/items */
public record AgregarItemCarritoRequest(

        @NotNull(message = "El id del libro es obligatorio")
        UUID idLibro,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        Integer cantidad
) {}

