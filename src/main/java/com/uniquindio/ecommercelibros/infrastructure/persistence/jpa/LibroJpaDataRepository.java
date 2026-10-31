package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.repository.LibroVentas;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;

import java.util.Collection;
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
        WHERE l.eliminado = false AND LOWER(l.titulo) LIKE LOWER(CONCAT('%', :palabraTitulo, '%'))
        AND l.estado = :estado
        AND l.eliminado = false
        """)
    Page<LibroEntity> listarLibros(@Param("palabraTitulo") String palabraTitulo,
                                   @Param("estado") EstadoLibro estado,
                                   Pageable pageable);

    @Query("""
        SELECT new com.uniquindio.ecommercelibros.domain.repository.LibroVentas(l.idLibro, l.titulo, COUNT(iv))
        FROM LibroEntity l
        JOIN VentaEntity v ON v.estadoVenta = :estado
        JOIN v.items iv ON l.idLibro = iv.isbn.isbn
        GROUP BY l.idLibro, l.titulo
        ORDER BY COUNT(iv) DESC, l.titulo ASC
        """)
    List<LibroVentas> buscarLibrosMasVendidos(@Param("estado") EstadoPedido estado, int limite);

    Optional<LibroEntity> findByIsbnAndEliminadoFalse(String isbn);
    Optional<LibroEntity> findByIdLibroAndEliminadoFalse(String idLibro);
    List<LibroEntity> findByTituloContainingIgnoreCaseAndEliminadoFalse(String titulo);
    List<LibroEntity> findByIdAutorAndEliminadoFalse(String idAutor);
    List<LibroEntity> findByIsbnInAndEliminadoFalse(Collection<String> isbns);
    boolean existsByIsbn(String isbn);
}
