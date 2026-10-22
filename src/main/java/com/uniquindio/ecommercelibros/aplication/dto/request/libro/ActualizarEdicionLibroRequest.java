package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.Size;

/** PUT /libros/{idLibro}/edicion */
public record ActualizarEdicionLibroRequest(
        @Size(max = 100, message = "La edición no puede superar los 100 caracteres")
        String edicion
) {}
