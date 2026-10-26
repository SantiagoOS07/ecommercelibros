package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** PUT /usuarios/{idUsuario}/nombre */
public record ActualizarNombreUsuarioRequest(
        @NotBlank(message = "El nombre del usuario es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre
) {}