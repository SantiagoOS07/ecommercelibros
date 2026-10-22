package com.uniquindio.ecommercelibros.aplication.dto.request.autor;

import jakarta.validation.constraints.NotBlank;

/** PUT /autores/{idAutor}/nombre */
public record ActualizarNombreAutorRequest(
        @NotBlank(message = "El nombre del autor es obligatorio")
        String nombreAutor
) {}
