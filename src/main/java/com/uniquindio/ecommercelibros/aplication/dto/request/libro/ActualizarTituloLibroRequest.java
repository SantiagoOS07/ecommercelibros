package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** PUT /libros/{idLibro}/titulo */
public record ActualizarTituloLibroRequest(
        @NotBlank(message = "El título del libro es obligatorio")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String titulo
) {}
