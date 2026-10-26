package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** PUT /usuarios/{idUsuario}/contrasenia */
public record CambiarContraseniaUsuarioRequest(
        @NotBlank(message = "La contraseña actual es obligatoria")
        String contraseniaActual,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, message = "La nueva contraseña debe tener mínimo 8 caracteres")
        String nuevaContrasenia
) {}