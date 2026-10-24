package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface VentaJpaDataRepository extends JpaRepository<VentaEntity, String> {
    List<VentaEntity> findByIdUsuarioOrderByFechaVentaDesc(String idUsuario);

    Optional<VentaEntity> findByIdVenta(String idVenta);
}
