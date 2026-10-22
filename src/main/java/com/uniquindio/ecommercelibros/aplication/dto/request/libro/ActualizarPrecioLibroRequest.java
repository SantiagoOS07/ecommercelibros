package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** PUT /libros/{idLibro}/precio */
public record ActualizarPrecioLibroRequest(
        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        Double precio,

        @NotNull(message = "La moneda es obligatoria")
        Moneda moneda
) {}
