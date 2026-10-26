package com.uniquindio.ecommercelibros.aplication.dto.response.usuario;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;

/** GET /usuarios, GET /usuarios/{idUsuario}, GET /usuarios/correo/{correo} y POST /usuarios */
public record UsuarioResponse(
        String idUsuario,
        String nombre,
        String correo,
        String telefono,
        String direccion,
        RolUsuario rolUsuario,
        EstadoUsuario estadoUsuario
) {}