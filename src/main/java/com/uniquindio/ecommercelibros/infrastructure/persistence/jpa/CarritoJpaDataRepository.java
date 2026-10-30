package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoJpaDataRepository
        extends JpaRepository<CarritoEntity, String> {

    Optional<CarritoEntity> findByIdCarritoAndEliminadoFalse(String idCarrito);

    Optional<CarritoEntity> findByIdUsuarioAndEstadoNotAndEliminadoFalse(String idUsuario, EstadoCarrito estado);

    List<CarritoEntity> findByEstadoAndEliminadoFalse(EstadoCarrito estado);

    List<CarritoEntity> findByEliminadoFalse();
}
