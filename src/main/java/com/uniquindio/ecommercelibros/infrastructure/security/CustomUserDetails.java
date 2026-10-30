package com.uniquindio.ecommercelibros.infrastructure.security;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.UsuarioEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails (UsuarioEntity usuarioEntity) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuarioEntity.getRolUsuario().name()));
    }

    @Override
    public @Nullable String getPassword() {
        return usuarioEntity.getContrasenia();
    }

    @Override
    public String getUsername() {
        return usuarioEntity.getCorreo();
    }

    @Override
    public boolean isEnabled() {
        return usuarioEntity.getEstadoUsuario().estaActivo();
    }

}
