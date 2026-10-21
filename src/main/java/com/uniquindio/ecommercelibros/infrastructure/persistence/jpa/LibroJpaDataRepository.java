package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroJpaDataRepository
        extends JpaRepository<LibroEntity, String> {

    Optional<LibroEntity> findByIsbn(String isbn);

    Optional<LibroEntity> findByTituloContainingIgnoreCase(String titulo);

    List<LibroEntity> findByIdAutor(String idAutor);

    List<LibroEntity> findByCategoria(com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro categoria);

    List<LibroEntity> findByEstado(EstadoLibro estado);
}
