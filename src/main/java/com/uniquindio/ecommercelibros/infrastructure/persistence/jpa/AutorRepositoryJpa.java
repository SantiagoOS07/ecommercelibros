package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.AutorPersistenceMapper;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class AutorRepositoryJpa implements AutorRepository {

    private final AutorJpaDataRepository dataRepository;
    private final AutorPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Autor> buscarPorIdAutor(String idAutor) {
        return dataRepository.findById(idAutor)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Autor> buscarPorNombre(String nombreAutor) {
        return dataRepository.findFirstByNombreAutorIgnoreCase(nombreAutor)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return dataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void guardarAutor(Autor autor) {
        dataRepository.save(mapper.toEntity(autor));
    }

    @Override
    public void eliminarAutor(String idAutor) {
        dataRepository.deleteById(idAutor);
    }
}