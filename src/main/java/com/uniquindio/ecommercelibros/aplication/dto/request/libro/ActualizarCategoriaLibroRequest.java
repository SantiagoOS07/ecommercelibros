package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import jakarta.validation.constraints.NotNull;

/** PUT /libros/{idLibro}/categoria */
public record ActualizarCategoriaLibroRequest(
        @NotNull(message = "La categoría es obligatoria")
        CategoriaLibro categoria
) {}
