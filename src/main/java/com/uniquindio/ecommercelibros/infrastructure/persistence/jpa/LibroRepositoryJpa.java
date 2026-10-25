package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.LibroPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class LibroRepositoryJpa implements LibroRepository {

    private final LibroJpaDataRepository dataRepository;
    private final LibroPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Libro> buscarPorISBN(String isbn) {
        return dataRepository.findByIsbn(isbn)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Libro> buscarPorIdLibro(UUID idLibro) {
        return dataRepository.findById(idLibro.toString())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarPorNombre(String nombre) {
        return dataRepository.findByTituloContainingIgnoreCase(nombre)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarPorAutor(String idAutor) {
        return dataRepository.findByIdAutor(idAutor)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void guardarLibro(Libro libro) {
        LibroEntity entity = dataRepository.findById(libro.getIdLibro().toString())
                .map(existente -> mapper.actualizarEntity(existente, libro))
                .orElseGet(() -> mapper.toEntity(libro));
        dataRepository.save(entity);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Libro> listarTodosLosLibros() {
        return dataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void eliminarLibro(UUID idLibro) {
        dataRepository.deleteById(idLibro.toString());
    }
}

