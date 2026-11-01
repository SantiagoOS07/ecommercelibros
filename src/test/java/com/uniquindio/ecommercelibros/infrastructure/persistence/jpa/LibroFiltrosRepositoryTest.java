package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.FiltroLibros;
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
import org.junit.jupiter.api.BeforeEach;
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
class LibroFiltrosRepositoryTest {

    private static final String PALABRA = "zorzal";

    @Autowired private LibroRepository repository;
    @Autowired private EntityManager entityManager;

    @BeforeEach
    void guardarLibrosDePrueba() {
        Libro eliminado = crearLibro("Zorzal E eliminado", "9780000000905", CategoriaLibro.NOVELA, 25, Moneda.USD);
        eliminado.eliminarLogicamente();

        for (Libro libro : List.of(
                crearLibro("Zorzal A novela", "9780000000901", CategoriaLibro.NOVELA, 20, Moneda.USD),
                crearLibro("Zorzal B novela cara", "9780000000902", CategoriaLibro.NOVELA, 60, Moneda.USD),
                crearLibro("Zorzal C historia", "9780000000903", CategoriaLibro.HISTORIA, 30, Moneda.USD),
                crearLibro("Zorzal D novela en pesos", "9780000000904", CategoriaLibro.NOVELA, 50000, Moneda.COP),
                eliminado)) {
            repository.guardarLibro(libro);
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void buscarLibros_sinFiltrosExtra_devuelveLosNoEliminadosOrdenadosPorTitulo() {
       
        Pagina<Libro> pagina = buscar(new FiltroLibros(PALABRA, null, null, null, null, null));

        
        assertEquals(List.of("Zorzal A novela", "Zorzal B novela cara", "Zorzal C historia", "Zorzal D novela en pesos"),
                titulos(pagina));
    }

    @Test
    void buscarLibros_porCategoria_soloDevuelveEsaCategoria() {
       
        Pagina<Libro> pagina = buscar(new FiltroLibros(PALABRA, null, CategoriaLibro.NOVELA, null, null, null));

        
        assertEquals(List.of("Zorzal A novela", "Zorzal B novela cara", "Zorzal D novela en pesos"), titulos(pagina));
    }

    @Test
    void buscarLibros_porRangoDePrecioEnUsd_incluyeLosExtremosYExcluyeOtraMoneda() {
       
        Pagina<Libro> pagina = buscar(new FiltroLibros(PALABRA, null, null, 20.0, 30.0, Moneda.USD));

        
        assertEquals(List.of("Zorzal A novela", "Zorzal C historia"), titulos(pagina));
    }

    @Test
    void buscarLibros_soloPrecioMinimo_devuelveLosDeEseMontoOMas() {
       
        Pagina<Libro> pagina = buscar(new FiltroLibros(PALABRA, null, null, 30.0, null, Moneda.USD));

        
        assertEquals(List.of("Zorzal B novela cara", "Zorzal C historia"), titulos(pagina));
    }

    @Test
    void buscarLibros_categoriaYPrecioCombinados_aplicaAmbos() {
       
        Pagina<Libro> pagina = buscar(new FiltroLibros(PALABRA, null, CategoriaLibro.NOVELA, 10.0, 40.0, Moneda.USD));

        
        assertEquals(List.of("Zorzal A novela"), titulos(pagina));
        assertEquals(1, pagina.totalElementos());
    }

    @Test
    void buscarLibros_soloMoneda_filtraPorMoneda() {
       
        Pagina<Libro> pagina = buscar(new FiltroLibros(PALABRA, null, null, null, null, Moneda.COP));

        
        assertEquals(List.of("Zorzal D novela en pesos"), titulos(pagina));
    }

    private Pagina<Libro> buscar(FiltroLibros filtro) {
        return repository.buscarLibros(filtro, 0, 10);
    }

    private static List<String> titulos(Pagina<Libro> pagina) {
        return pagina.contenido().stream().map(libro -> libro.getTitulo().nombreLibro()).toList();
    }

    private static Libro crearLibro(String titulo, String isbn, CategoriaLibro categoria, double precio, Moneda moneda) {
        return Libro.crear(
                new NombreLibro(titulo),
                "autor-filtros",
                new ISBN(isbn),
                "Descripcion de prueba",
                new NumeroPaginas(200),
                new Precio(precio, moneda),
                new Stock(5),
                "Primera edicion",
                UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(2021, 3, 15)),
                categoria,
                "https://imagenes.test/libro.png",
                EstadoLibro.DISPONIBLE
        );
    }
}