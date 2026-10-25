package com.uniquindio.ecommercelibros.infraestructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LibroRepositoryIntegracionTest {

    private static final LocalDate FECHA = LocalDate.of(2020, 5, 10);

    @Autowired
    private LibroRepository repositorio; // el puerto del dominio

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Guardar y recuperar un libro devuelve los mismos datos, incluido el Precio")
    void guardarYRecuperar_devuelveLosMismosDatos() {
        // Arrange
        Libro original = libro("9780306406157", "Fundamentos de programación");

        // Act
        repositorio.guardarLibro(original);
        em.flush();
        em.clear(); // fuerza a leer de la base de datos y no de la caché
        Optional<Libro> recuperado = repositorio.buscarPorIdLibro(original.getIdLibro());

        // Assert
        assertThat(recuperado).isPresent();
        Libro libro = recuperado.get();
        assertThat(libro.getIdLibro()).isEqualTo(original.getIdLibro());
        assertThat(libro.getTitulo().nombreLibro()).isEqualTo("Fundamentos de programación");
        assertThat(libro.getIsbn().isbn()).isEqualTo("9780306406157");
        assertThat(libro.getIdAutor()).isEqualTo(original.getIdAutor());
        assertThat(libro.getIdEditorial()).isEqualTo(original.getIdEditorial());
        assertThat(libro.getNumeroPaginas().numeroPaginas()).isEqualTo(320);
        assertThat(libro.getStock().stock()).isEqualTo(12);
        assertThat(libro.getFechaPublicacion().fecha()).isEqualTo(FECHA);
        assertThat(libro.getCategoria()).isEqualTo(CategoriaLibro.values()[0]);
        assertThat(libro.getEstado()).isEqualTo(EstadoLibro.values()[0]);
        assertThat(libro.getPrecio().monto()).isEqualTo(45000.50);
        assertThat(libro.getPrecio().moneda()).isEqualTo("COP");
    }

    @Test
    @DisplayName("La paginación devuelve la cantidad y el orden esperados")
    void paginacion_devuelveCantidadYOrdenEsperados() {
        // Arrange: 5 libros en desorden; el orden alfabético por título es conocido
        repositorio.guardarLibro(libro("9780201633610", "Compiladores modernos"));
        repositorio.guardarLibro(libro("9781491950296", "Sistemas operativos"));
        repositorio.guardarLibro(libro("9780306406157", "Algoritmos y estructuras"));
        repositorio.guardarLibro(libro("9780596520687", "Redes de computadores"));
        repositorio.guardarLibro(libro("9780131103627", "Bases de datos relacionales"));
        em.flush();
        em.clear();

        // Act
        Page<Libro> pagina0 = repositorio.listarPaginado(PageRequest.of(0, 2, Sort.by("titulo")));
        Page<Libro> pagina1 = repositorio.listarPaginado(PageRequest.of(1, 2, Sort.by("titulo")));
        Page<Libro> pagina2 = repositorio.listarPaginado(PageRequest.of(2, 2, Sort.by("titulo")));

        // Assert
        assertThat(pagina0.getTotalElements()).isEqualTo(5);
        assertThat(pagina0.getTotalPages()).isEqualTo(3);
        assertThat(titulos(pagina0)).containsExactly("Algoritmos y estructuras", "Bases de datos relacionales");
        assertThat(titulos(pagina1)).containsExactly("Compiladores modernos", "Redes de computadores");
        assertThat(titulos(pagina2)).containsExactly("Sistemas operativos");
    }

    private static List<String> titulos(Page<Libro> pagina) {
        return pagina.getContent().stream().map(l -> l.getTitulo().nombreLibro()).toList();
    }

    private static Libro libro(String isbn, String titulo) {
        return Libro.crear(
                new NombreLibro(titulo),
                UUID.randomUUID().toString(),
                new ISBN(isbn),
                "Descripción de prueba",
                new NumeroPaginas(320),
                new Precio(45000.50, "COP"),
                new Stock(12),
                "Primera edición",
                UUID.randomUUID(),
                new FechaPublicacion(FECHA),
                CategoriaLibro.values()[0],
                "https://imagenes.test/libro.png",
                EstadoLibro.values()[0]);
    }
}