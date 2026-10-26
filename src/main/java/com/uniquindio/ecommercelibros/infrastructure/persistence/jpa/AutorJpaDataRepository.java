package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.AutorEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorJpaDataRepository
        extends JpaRepository<AutorEntity, String> {

    Optional<AutorEntity> findFirstByNombreAutorIgnoreCase(String nombreAutor);
}