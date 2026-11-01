package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.FiltroLibros;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroVentas;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.LibroPersistenceMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class LibroRepositoryJpa implements LibroRepository {
    

    private final LibroJpaDataRepository dataRepository;
    private final LibroPersistenceMapper mapper;
    private static final BigDecimal PRECIO_MAXIMO_COLUMNA = new BigDecimal("9999999999.99");

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
    @Transactional(readOnly = true)
    public Pagina<Libro> buscarLibros(FiltroLibros filtro, int pagina, int tamanio) {
        String palabra = (filtro.palabraTitulo() == null) ? "" : filtro.palabraTitulo().trim();
        EstadoLibro estado = (filtro.estado() == null) ? EstadoLibro.DISPONIBLE : filtro.estado();
        Collection<CategoriaLibro> categorias = (filtro.categoria() == null)
                ? EnumSet.allOf(CategoriaLibro.class) : List.of(filtro.categoria());
        Collection<Moneda> monedas = (filtro.moneda() == null)
                ? EnumSet.allOf(Moneda.class) : List.of(filtro.moneda());
        BigDecimal precioMinimo = (filtro.precioMinimo() == null)
                ? BigDecimal.ZERO : BigDecimal.valueOf(filtro.precioMinimo());
        BigDecimal precioMaximo = (filtro.precioMaximo() == null)
                ? PRECIO_MAXIMO_COLUMNA : BigDecimal.valueOf(filtro.precioMaximo());

        Page<LibroEntity> resultado = dataRepository.listarLibros(
                palabra, estado, categorias, monedas, precioMinimo, precioMaximo,
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
    @Transactional(readOnly = true)
    public Map<String, String> buscarTitulosPorIsbn(Collection<String> isbns) {
        if (isbns == null || isbns.isEmpty()) {
            return Map.of();
        }
        return dataRepository.findByIsbnInAndEliminadoFalse(isbns).stream()
                .collect(Collectors.toMap(
                        LibroEntity::getIsbn,
                        LibroEntity::getTitulo,
                        (a, b) -> a));
    }

    @Override
    public List<LibroVentas> buscarLibrosMasVendidos(int limite) {
        return dataRepository.buscarLibrosMasVendidos(EstadoPedido.ENTREGADO, PageRequest.of(0, limite));
    }
}


