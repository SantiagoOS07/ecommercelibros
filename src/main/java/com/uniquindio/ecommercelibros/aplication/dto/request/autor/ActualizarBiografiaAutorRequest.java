package com.uniquindio.ecommercelibros.aplication.dto.request.autor;

import jakarta.validation.constraints.Size;

/** PUT /autores/{idAutor}/biografia */
public record ActualizarBiografiaAutorRequest(
        @Size(max = 2000, message = "La biografía no puede superar los 2000 caracteres")
        String biografia
) {}
