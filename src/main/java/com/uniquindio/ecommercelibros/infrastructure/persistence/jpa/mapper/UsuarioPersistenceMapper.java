package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setNombre(usuario.getNombre());
        entity.setCorreo(usuario.getCorreo().correo());
        entity.setContrasenia(usuario.getContrasenia());
        entity.setTelefono(usuario.getTelefono().telefono());
        entity.setDireccion(usuario.getDireccion().direccion());
        entity.setRolUsuario(usuario.getRolUsuario());
        entity.setEstadoUsuario(usuario.getEstadoUsuario());
        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        return Usuario.reconstruir(
                entity.getIdUsuario(),
                entity.getNombre(),
                new Correo(entity.getCorreo()),
                entity.getContrasenia(),
                new Telefono(entity.getTelefono()),
                new Direccion(entity.getDireccion()),
                entity.getRolUsuario(),
                entity.getEstadoUsuario()
        );
    }
}