package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoJpaDataRepository
        extends JpaRepository<CarritoEntity, String> {

    Optional<CarritoEntity> findByIdUsuario(String idUsuario);

    List<CarritoEntity> findByEstado(EstadoCarrito estadoCarrito);

    Optional<CarritoEntity> findByIdUsuarioAndEstadoNot(String idUsuario, EstadoCarrito estadoCarrito);
}
