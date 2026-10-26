package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.FechaPublicacion;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.NombreLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.NumeroPaginas;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.LibroPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({LibroRepositoryJpa.class, LibroPersistenceMapper.class})
class LibroJpaDataRepositoryTest {

    @Autowired
    private LibroRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void deberiaBuscarLibrosPorTextoEnElTitulo() {
        guardar(
                crearLibro("El dragon de fuego", "9780000000401", EstadoLibro.DISPONIBLE),
                crearLibro("Dragon de hielo", "9780000000402", EstadoLibro.DISPONIBLE),
                crearLibro("La casa del dragon", "9780000000403", EstadoLibro.AGOTADO),
                crearLibro("Cien anios de soledad", "9780000000404", EstadoLibro.DISPONIBLE)
        );

        List<Libro> encontrados = repository.buscarPorNombre("dragon");

        assertEquals(3, encontrados.size());
    }

    @Test
    void deberiaPaginarSoloLosLibrosDisponiblesOrdenadosPorTitulo() {
        guardar(
                crearLibro("Mago C", "9780000000501", EstadoLibro.DISPONIBLE),
                crearLibro("Mago A", "9780000000502", EstadoLibro.DISPONIBLE),
                crearLibro("Mago B", "9780000000503", EstadoLibro.DISPONIBLE),
                crearLibro("Mago D", "9780000000504", EstadoLibro.AGOTADO)
        );

        Pagina<Libro> pagina = repository.buscarLibros("mago", EstadoLibro.DISPONIBLE, 0, 2);

        assertEquals(3, pagina.totalElementos());
        assertEquals(2, pagina.totalPaginas());
        assertEquals(List.of("Mago A", "Mago B"), pagina.contenido().stream()
                .map(libro -> libro.getTitulo().nombreLibro())
                .toList());
    }

    private void guardar(Libro... libros) {
        for (Libro libro : libros) {
            repository.guardarLibro(libro);
        }
        entityManager.flush();
        entityManager.clear();
    }

    private static Libro crearLibro(String titulo, String isbn, EstadoLibro estado) {
        return Libro.crear(
                new NombreLibro(titulo),
                "autor-test",
                new ISBN(isbn),
                "Descripcion de prueba",
                new NumeroPaginas(200),
                new Precio(30000, Moneda.COP),
                new Stock(5),
                "Primera edicion",
                UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(2021, 3, 15)),
                CategoriaLibro.NOVELA,
                "https://imagenes.test/libro.png",
                estado
        );
    }
}