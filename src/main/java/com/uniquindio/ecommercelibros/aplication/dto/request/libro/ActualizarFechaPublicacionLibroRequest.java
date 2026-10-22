package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/** PUT /libros/{idLibro}/fecha-publicacion */
public record ActualizarFechaPublicacionLibroRequest(
        @NotNull(message = "La fecha de publicación es obligatoria")
        @PastOrPresent(message = "La fecha de publicación no puede ser posterior a la fecha actual")
        LocalDate fechaPublicacion
) {}
