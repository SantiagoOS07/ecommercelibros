package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// PUT /api/libros/{idLibro}/descuento
public record AplicarDescuentoLibroRequest (
        @NotNull(message = "El porcentaje esta vacio")
        @Positive(message = "El descuento debe ser positivo")
        @Max(99)
        Double porcentaje,

        @NotBlank(message = "El motivo no puede ser vacio")
        @Size(max = 100, message = "El título no puede superar los 150 caracteres")
        String motivo,

        @NotNull(message = "La fecha de inicio esta vacia")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin esta vacia")
        LocalDate fechaFin
        )

{}
