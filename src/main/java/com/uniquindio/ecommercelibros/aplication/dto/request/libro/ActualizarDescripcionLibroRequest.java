package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.Size;

/** PUT /libros/{idLibro}/descripcion */
public record ActualizarDescripcionLibroRequest(
        @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
        String descripcion
) {}
