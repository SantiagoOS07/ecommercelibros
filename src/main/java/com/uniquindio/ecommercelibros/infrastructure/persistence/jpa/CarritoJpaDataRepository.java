package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoJpaDataRepository
        extends JpaRepository<CarritoEntity, String> {

    Optional<CarritoEntity> findByIdUsuario(String idUsuario);

    Optional<CarritoEntity> findByEstado(EstadoCarrito estadoCarrito);
}
