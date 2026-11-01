package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.repository.LibroVentas;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;

import java.math.BigDecimal;
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

        /**
     * Listado paginado. Nunca devuelve libros eliminados.
     * Los filtros opcionales llegan siempre con valor (todas las categorías/monedas, rango 0..máximo)
     * para no depender de "(:param IS NULL OR ...)", que H2 rechaza con parámetros sin tipo.
     */
    @Query("""
        SELECT l FROM LibroEntity l
        WHERE l.eliminado = false
        AND LOWER(l.titulo) LIKE LOWER(CONCAT('%', :palabraTitulo, '%'))
        AND l.estado = :estado
        AND l.categoria IN :categorias
        AND l.precio.moneda IN :monedas
        AND l.precio.monto BETWEEN :precioMinimo AND :precioMaximo
        """)
    Page<LibroEntity> listarLibros(@Param("palabraTitulo") String palabraTitulo,
                                   @Param("estado") EstadoLibro estado,
                                   @Param("categorias") Collection<CategoriaLibro> categorias,
                                   @Param("monedas") Collection<Moneda> monedas,
                                   @Param("precioMinimo") BigDecimal precioMinimo,
                                   @Param("precioMaximo") BigDecimal precioMaximo,
                                   Pageable pageable);

    @Query("""
        SELECT new com.uniquindio.ecommercelibros.domain.repository.LibroVentas(l.idLibro, l.titulo, COUNT(iv))
        FROM LibroEntity l
        JOIN VentaEntity v ON v.estadoVenta = :estado
        JOIN v.items iv ON l.isbn = iv.isbn.isbn
        GROUP BY l.idLibro, l.titulo
        ORDER BY COUNT(iv) DESC, l.titulo ASC
        """)
    List<LibroVentas> buscarLibrosMasVendidos(@Param("estado") EstadoPedido estado, Pageable pageable);

    Optional<LibroEntity> findByIsbnAndEliminadoFalse(String isbn);
    Optional<LibroEntity> findByIdLibroAndEliminadoFalse(String idLibro);
    List<LibroEntity> findByTituloContainingIgnoreCaseAndEliminadoFalse(String titulo);
    List<LibroEntity> findByIdAutorAndEliminadoFalse(String idAutor);
    List<LibroEntity> findByIsbnInAndEliminadoFalse(Collection<String> isbns);
    boolean existsByIsbn(String isbn);
}
