package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.usuario.UsuarioResponse;
import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

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


    public Correo toCorreo(String correo) {
        return new Correo(correo == null ? null : correo.trim().toLowerCase(Locale.ROOT));
    }

    public Telefono toTelefono(String telefono) {
        return new Telefono(telefono == null ? null : telefono.trim());
    }

    public Direccion toDireccion(String direccion) {
        return new Direccion(direccion == null ? null : direccion.trim());
    }
}