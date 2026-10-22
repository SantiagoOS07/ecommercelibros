package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import jakarta.validation.constraints.NotNull;

/** PUT	/libros/{idLibro}/agotar, PUT /libros/{idLibro}/reactivar, PUT /libros/{idLibro}/descontinuar */
public record ActualizarEstadoLibroRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoLibro estado
) {}
