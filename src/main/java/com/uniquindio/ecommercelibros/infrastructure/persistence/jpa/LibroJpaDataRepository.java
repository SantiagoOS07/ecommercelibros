package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibroJpaDataRepository
        extends JpaRepository<LibroEntity, String> {

    Optional<LibroEntity> findByIsbn(String isbn);

    List<LibroEntity> findByTituloContainingIgnoreCase(String titulo);

    List<LibroEntity> findByIdAutor(String idAutor);

    List<LibroEntity> findByCategoria(com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro categoria);

    List<LibroEntity> findByEstado(EstadoLibro estado);

    @Query("""
        SELECT l FROM LibroEntity l
        WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :palabraTitulo, '%'))
        AND l.estado = :estado
        """)
    Page<LibroEntity> listarLibros(@Param("palabraTitulo") String palabraTitulo,
                                   @Param("estado") EstadoLibro estado,
                                   Pageable pageable);
}
