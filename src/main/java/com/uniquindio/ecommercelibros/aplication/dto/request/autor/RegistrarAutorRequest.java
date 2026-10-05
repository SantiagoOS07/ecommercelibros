package com.uniquindio.ecommercelibros.aplication.dto.request.autor;

import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** POST /autores */
public record RegistrarAutorRequest(
        @NotBlank(message = "El id del autor es obligatorio")
        String idAutor,

        @NotBlank(message = "El nombre del autor es obligatorio")
        String nombreAutor,

        @Size(max = 2000, message = "La biografía no puede superar los 2000 caracteres")
        String biografia,

        @NotNull(message = "La nacionalidad es obligatoria")
        Nacionalidad nacionalidad
) {}
