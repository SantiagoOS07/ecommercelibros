package com.uniquindio.ecommercelibros.aplication.dto.request.editorial;

import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** POST /api/editoriales */
public record RegistrarEditorialRequest(
        @NotBlank(message = "El nombre de la editorial es obligatorio")
        @Size(max = 150, message = "El nombre de la editorial no puede superar los 150 caracteres")
        String nombreEditorial,

        @NotNull(message = "El país de origen es obligatorio")
        Pais paisOrigen,

        @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
        String descripcionEditorial
) {}