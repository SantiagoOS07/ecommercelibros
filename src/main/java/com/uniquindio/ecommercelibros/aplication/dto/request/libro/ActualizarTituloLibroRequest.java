package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.NotBlank;

/** PUT /libros/{idLibro}/titulo */
public record ActualizarTituloLibroRequest(
        @NotBlank(message = "El título del libro es obligatorio")
        String titulo
) {}
