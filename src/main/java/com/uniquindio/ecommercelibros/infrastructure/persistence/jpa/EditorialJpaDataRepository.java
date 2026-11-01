package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.EditorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EditorialJpaDataRepository extends JpaRepository<EditorialEntity, String> {

    boolean existsByNombreEditorialIgnoreCase(String nombreEditorial);

    List<EditorialEntity> findAllByOrderByNombreEditorialAsc();
}