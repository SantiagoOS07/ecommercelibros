package com.uniquindio.ecommercelibros.aplication.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank(message = "El usuario es obligatorio")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasenia
)
{}
