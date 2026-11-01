package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.AutorEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorJpaDataRepository
        extends JpaRepository<AutorEntity, String> {

    Optional<AutorEntity> findByIdAutorAndEliminadoFalse(String idAutor);

    Optional<AutorEntity> findFirstByNombreAutorIgnoreCaseAndEliminadoFalse(String nombreAutor);

    List<AutorEntity> findByEliminadoFalseOrderByNombreAutorAsc();
}