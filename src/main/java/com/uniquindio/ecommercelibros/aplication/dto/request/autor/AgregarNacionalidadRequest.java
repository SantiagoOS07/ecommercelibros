package com.uniquindio.ecommercelibros.aplication.dto.request.autor;

import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import jakarta.validation.constraints.NotNull;

/** POST /autores/{idAutor}/nacionalidades */
public record AgregarNacionalidadRequest(
        @NotNull(message = "La nacionalidad es obligatoria")
        Nacionalidad nacionalidad
) {}
