package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** PUT /usuarios/{idUsuario}/direccion */
public record ActualizarDireccionUsuarioRequest(
        @NotBlank(message = "La dirección del usuario es obligatoria")
        @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
        String direccion
) {}