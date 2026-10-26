package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import jakarta.validation.constraints.NotNull;

/** PUT /usuarios/{idUsuario}/rol */
public record ActualizarRolUsuarioRequest(
        @NotNull(message = "El rol del usuario es obligatorio")
        RolUsuario rolUsuario
) {}