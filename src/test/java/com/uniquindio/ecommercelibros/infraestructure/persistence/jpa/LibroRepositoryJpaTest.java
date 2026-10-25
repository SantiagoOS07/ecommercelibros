package com.uniquindio.ecommercelibros.infraestructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.LibroJpaDataRepository;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.LibroRepositoryJpa;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.LibroPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({LibroRepositoryJpa.class, LibroPersistenceMapper.class})
class LibroRepositoryJpaTest {

    private static final LocalDate FECHA = LocalDate.of(2020, 5, 10);

    @Autowired
    private LibroRepositoryJpa repositorio;

    @Autowired
    private LibroJpaDataRepository dataRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Buscar por nombre ignora mayúsculas y devuelve solo las coincidencias")
    void buscarPorNombre_ignoraMayusculasYFiltra() {
        // Arrange
        repositorio.guardarLibro(libro("9780306406157", "Algoritmos y estructuras"));
        repositorio.guardarLibro(libro("9780131103627", "Bases de datos relacionales"));
        em.flush();
        em.clear();

        // Act
        List<Libro> encontrados = repositorio.buscarPorNombre("ALGORITMOS");

        // Assert
        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getTitulo().nombreLibro()).isEqualTo("Algoritmos y estructuras");
    }

    private static List<String> titulos(Page<LibroEntity> pagina) {
        return pagina.getContent().stream().map(LibroEntity::getTitulo).toList();
    }

    private static Libro libro(String isbn, String titulo) {
        return Libro.crear(
                new NombreLibro(titulo),
                UUID.randomUUID().toString(),
                new ISBN(isbn),
                "Descripción de prueba",
                new NumeroPaginas(320),
                new Precio(4500.50, Moneda.COP),
                new Stock(12),
                "Primera edición",
                UUID.randomUUID(),
                new FechaPublicacion(FECHA),
                CategoriaLibro.values()[0],
                "https://imagenes.test/libro.png",
                EstadoLibro.values()[0]);
    }
}