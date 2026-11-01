package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.EditorialPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional
public class EditorialRepositoryJpa implements EditorialRepository {

    private final EditorialJpaDataRepository dataRepository;
    private final EditorialPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Editorial> buscarPorId(UUID idEditorial) {
        return dataRepository.findById(idEditorial.toString())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Editorial> listarTodas() {
        return dataRepository.findAllByOrderByNombreEditorialAsc()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void guardarEditorial(Editorial editorial) {
        dataRepository.save(mapper.toEntity(editorial));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombreEditorial) {
        return dataRepository.existsByNombreEditorialIgnoreCase(nombreEditorial);
    }
}