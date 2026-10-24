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

@Repository
@RequiredArgsConstructor
public class LibroRepositoryJpa implements LibroRepository {

    private final LibroJpaDataRepository dataRepository;
    private final LibroPersistenceMapper mapper;

    @Override
    public Optional<Libro> buscarPorISBN(String isbn) {
        return dataRepository.findByIsbn(isbn)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Libro> buscarPorIdLibro(UUID idLibro) {
        return dataRepository.findById(idLibro.toString())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Libro> buscarPorNombre(String nombre) {
        return dataRepository.findByTituloContainingIgnoreCase(nombre)
                .map(mapper::toDomain);
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) {
        throw new UnsupportedOperationException(
                "Implementar búsqueda por nombre de autor mediante AutorRepository"
        );
    }

    @Override
    public void guardarLibro(Libro libro) {
        dataRepository.save(mapper.toEntity(libro));
    }

    @Override
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

