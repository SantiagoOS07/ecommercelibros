package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.CarritoPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class CarritoRepositoryJpa implements CarritoRepository {

    private final CarritoJpaDataRepository dataRepository;
    private final CarritoPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrito> buscarPorIdCarrito(UUID idCarrito) {

        return dataRepository.findByIdCarritoAndEliminadoFalse(idCarrito.toString()).map(mapper::toDomain);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrito> buscarPorIdUsuario(String idUsuario) {

        return dataRepository
                .findByIdUsuarioAndEstadoNotAndEliminadoFalse(idUsuario, EstadoCarrito.VENDIDO)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carrito> listarTodos() {

        return dataRepository.findByEliminadoFalse().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carrito> listarCarritosEnCreado() {
        return dataRepository.findByEstadoAndEliminadoFalse(EstadoCarrito.CREADO)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void guardarCarrito(Carrito carrito) {

        CarritoEntity entity = mapper.toEntity(carrito);
        dataRepository.save(entity);
    }

    @Override
    public List<Carrito> listarCarritosEnProcesando() {
        return dataRepository.findByEstadoAndEliminadoFalse(EstadoCarrito.PROCESANDO)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
