package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** PUT /libros/{idLibro}/editorial */
public record ActualizarEditorialLibroRequest(
        @NotNull(message = "El id de la editorial es obligatorio")
        UUID idEditorial
) {}
