package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** PUT /libros/{idLibro}/numero-paginas */
public record ActualizarNumeroPaginasLibroRequest(
        @NotNull(message = "El número de páginas es obligatorio")
        @Positive(message = "El número de páginas debe ser mayor a cero")
        Integer numeroPaginas
) {}
