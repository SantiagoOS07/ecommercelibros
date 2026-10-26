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
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class LibroRepositoryJpaTest {

    @Autowired
    private LibroRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void deberiaGuardarYRecuperarUnLibroConSusValueObjects() {
        Libro libro = crearLibro("Fundamentos de programacion", "9780000000011", EstadoLibro.DISPONIBLE);

        repository.guardarLibro(libro);
        sincronizar();
        Libro recuperado = repository.buscarPorIdLibro(libro.getIdLibro()).orElseThrow();

        assertEquals(libro.getIdLibro(), recuperado.getIdLibro());
        assertEquals(new NombreLibro("Fundamentos de programacion"), recuperado.getTitulo());
        assertEquals(new ISBN("9780000000011"), recuperado.getIsbn());
        assertEquals(new Precio(45000.50, Moneda.COP), recuperado.getPrecio());
        assertEquals(new Stock(12), recuperado.getStock());
        assertEquals(new NumeroPaginas(320), recuperado.getNumeroPaginas());
        assertEquals(new FechaPublicacion(LocalDate.of(2020, 5, 10)), recuperado.getFechaPublicacion());
        assertEquals(CategoriaLibro.PROGRAMACION, recuperado.getCategoria());
        assertEquals(EstadoLibro.DISPONIBLE, recuperado.getEstado());
        assertEquals(libro.getIdEditorial(), recuperado.getIdEditorial());
    }

    @Test
    void deberiaEncontrarUnLibroPorSuIsbn() {
        Libro libro = crearLibro("Libro con ISBN", "9780000000028", EstadoLibro.DISPONIBLE);
        repository.guardarLibro(libro);
        sincronizar();

        Libro encontrado = repository.buscarPorISBN("9780000000028").orElseThrow();

        assertEquals(libro.getIdLibro(), encontrado.getIdLibro());
        assertTrue(repository.buscarPorISBN("9789999999999").isEmpty());
    }

    @Test
    void deberiaPaginarOrdenadoPorTituloYContarElTotal() {
        guardar(
                crearLibro("Paginacion D", "9780000000103", EstadoLibro.DISPONIBLE),
                crearLibro("Paginacion B", "9780000000101", EstadoLibro.DISPONIBLE),
                crearLibro("Paginacion E", "9780000000104", EstadoLibro.DISPONIBLE),
                crearLibro("Paginacion A", "9780000000100", EstadoLibro.DISPONIBLE),
                crearLibro("Paginacion C", "9780000000102", EstadoLibro.DISPONIBLE),
                crearLibro("Paginacion F", "9780000000105", EstadoLibro.AGOTADO),
                crearLibro("Otro libro cualquiera", "9780000000106", EstadoLibro.DISPONIBLE)
        );

        Pagina<Libro> primera = repository.buscarLibros("paginacion", EstadoLibro.DISPONIBLE, 0, 2);
        Pagina<Libro> ultima = repository.buscarLibros("paginacion", EstadoLibro.DISPONIBLE, 2, 2);

        assertEquals(5, primera.totalElementos());
        assertEquals(3, primera.totalPaginas());
        assertEquals(0, primera.numero());
        assertEquals(2, primera.tamano());
        assertEquals(List.of("Paginacion A", "Paginacion B"), titulos(primera));
        assertEquals(2, ultima.numero());
        assertEquals(List.of("Paginacion E"), titulos(ultima));
    }

    @Test
    void deberiaFiltrarPorEstadoYUsarDisponibleCuandoElEstadoEsNulo() {
        guardar(
                crearLibro("Filtro A", "9780000000201", EstadoLibro.DISPONIBLE),
                crearLibro("Filtro B", "9780000000202", EstadoLibro.AGOTADO),
                crearLibro("Filtro C", "9780000000203", EstadoLibro.DESCONTINUADO)
        );

        Pagina<Libro> agotados = repository.buscarLibros("filtro", EstadoLibro.AGOTADO, 0, 10);
        Pagina<Libro> porDefecto = repository.buscarLibros("filtro", null, 0, 10);

        assertEquals(List.of("Filtro B"), titulos(agotados));
        assertEquals(List.of("Filtro A"), titulos(porDefecto));
    }

    private void guardar(Libro... libros) {
        for (Libro libro : libros) {
            repository.guardarLibro(libro);
        }
        sincronizar();
    }

    private void sincronizar() {
        entityManager.flush();
        entityManager.clear();
    }

    private static List<String> titulos(Pagina<Libro> pagina) {
        return pagina.contenido().stream()
                .map(libro -> libro.getTitulo().nombreLibro())
                .toList();
    }

    private static Libro crearLibro(String titulo, String isbn, EstadoLibro estado) {
        return Libro.crear(
                new NombreLibro(titulo),
                "autor-test",
                new ISBN(isbn),
                "Descripcion de prueba",
                new NumeroPaginas(320),
                new Precio(45000.50, Moneda.COP),
                new Stock(12),
                "Primera edicion",
                UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(2020, 5, 10)),
                CategoriaLibro.PROGRAMACION,
                "https://imagenes.test/libro.png",
                estado
        );
    }
}
