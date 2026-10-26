package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.UsuarioEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaDataRepository
        extends JpaRepository<UsuarioEntity, String> {

    Optional<UsuarioEntity> findByCorreoIgnoreCase(String correo);
}