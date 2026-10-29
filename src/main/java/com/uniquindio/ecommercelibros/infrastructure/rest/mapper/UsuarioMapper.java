package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.usuario.UsuarioResponse;
import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo().correo(),
                usuario.getTelefono().telefono(),
                usuario.getDireccion().direccion(),
                usuario.getRolUsuario(),
                usuario.getEstadoUsuario()
        );
    }

    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toResponse)
                .toList();
    }
}