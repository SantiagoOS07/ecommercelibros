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

@Repository
@RequiredArgsConstructor
public class CarritoRepositoryJpa implements CarritoRepository {

    private final CarritoJpaDataRepository dataRepository;
    private final CarritoPersistenceMapper mapper;

    @Override
    public Optional<Carrito> buscarPorIdCarrito(UUID idCarrito) {

        return dataRepository.findById(idCarrito.toString())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Carrito> buscarPorIdUsuario(String idUsuario) {

        return dataRepository.findByIdUsuario(idUsuario)
                .map(mapper::toDomain);
    }

    @Override
    public List<Carrito> listarTodos() {

        return dataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Carrito> listarCarritosEnCreado() {
        return dataRepository.findByEstado(EstadoCarrito.CREADO)
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
    public void eliminarCarrito(UUID idCarrito) {

        dataRepository.deleteById(idCarrito.toString());
    }
}

