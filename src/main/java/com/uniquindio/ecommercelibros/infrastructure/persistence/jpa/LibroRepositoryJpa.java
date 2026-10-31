package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroVentas;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.LibroPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return dataRepository.findByIsbnAndEliminadoFalse(isbn)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Libro> buscarPorIdLibro(UUID idLibro) {
        return dataRepository.findByIdLibroAndEliminadoFalse(idLibro.toString())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarPorNombre(String nombre) {
        return dataRepository.findByTituloContainingIgnoreCaseAndEliminadoFalse(nombre)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarPorAutor(String idAutor) {
        return dataRepository.findByIdAutorAndEliminadoFalse(idAutor)
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
    public boolean existePorIsbn(String isbn) {
        return dataRepository.existsByIsbn(isbn);
    }

    @Override
    public Pagina<Libro> buscarLibros(String palabraTitulo, EstadoLibro estado, int pagina, int tamanio) {
        String palabra = (palabraTitulo == null) ? "" : palabraTitulo.trim();
        if (estado == null) {
            estado = EstadoLibro.DISPONIBLE;
        }
        Page<LibroEntity> resultado = dataRepository.listarLibros(palabra, estado,
                PageRequest.of(pagina, tamanio, Sort.by("titulo")));
        return new Pagina<>(
                resultado.getContent().stream()
                        .map(mapper::toDomain)
                        .toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize()
        );
    }

    @Override
    public List<LibroVentas> buscarLibrosMasVendidos(int limite) {
        return dataRepository.buscarLibrosMasVendidos(EstadoPedido.ENTREGADO,limite);
    }
}


