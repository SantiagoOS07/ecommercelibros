package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** PUT /usuarios/{idUsuario}/correo */
public record ActualizarCorreoUsuarioRequest(
        @NotBlank(message = "El correo del usuario es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
        String correo
) {}