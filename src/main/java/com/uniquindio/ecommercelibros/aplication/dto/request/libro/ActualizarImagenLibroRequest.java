package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.Size;

/** PUT /libros/{idLibro}/imagen */
public record ActualizarImagenLibroRequest(
        @Size(max = 500, message = "La imagen no puede superar los 500 caracteres")
        String imagen
) {}
