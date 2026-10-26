package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** PUT /usuarios/{idUsuario}/telefono */
public record ActualizarTelefonoUsuarioRequest(
        @NotBlank(message = "El teléfono del usuario es obligatorio")
        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
        String telefono
) {}