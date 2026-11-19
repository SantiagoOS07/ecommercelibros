package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.VentaPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional
public class VentaRepositoryJpa implements VentaRepository {

    private final VentaJpaDataRepository dataRepository;
    private final VentaPersistenceMapper mapper;

    @Override
    public void guardarVenta(Venta venta) {
        dataRepository.save(mapper.toEntity(venta));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarVentaPorId(UUID id) {
        return dataRepository.findById(id.toString()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarVentasPorIdUsuario(String idUsuario) {
        return dataRepository.findByIdUsuarioOrderByFechaVentaDesc(idUsuario).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
