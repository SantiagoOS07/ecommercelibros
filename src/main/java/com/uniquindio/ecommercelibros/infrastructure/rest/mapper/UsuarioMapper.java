package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.RegistrarUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.usuario.UsuarioResponse;
import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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

    public Usuario toDomain(RegistrarUsuarioRequest request) {
        return Usuario.crear(
                UUID.randomUUID().toString(),
                request.nombre(),
                new Correo(request.correo()),
                request.contrasenia(),
                new Telefono(request.telefono()),
                new Direccion(request.direccion()),
                request.rolUsuario()
        );
    }
}