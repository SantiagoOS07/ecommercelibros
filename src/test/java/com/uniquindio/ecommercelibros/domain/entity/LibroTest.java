package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void dosLibrosConDatosDiferentesDebenSerEntidadesDiferentes() {

        Libro libro1 = crearLibro();
        Libro libro2 = crearLibro();
        boolean sonDiferentes = !libro1.equals(libro2);
        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearLibroSinISBN() {

        NombreLibro titulo = new NombreLibro("El Principito");
        String idAutor = "autor-123";
        String descripcion = "Una historia clásica";
        NumeroPaginas paginas = new NumeroPaginas(120);
        Precio precio = new Precio(25000, Moneda.COP);
        Stock stock = new Stock(10);
        UUID idEditorial = UUID.randomUUID();
        FechaPublicacion fecha = new FechaPublicacion(
                LocalDate.of(1943, 4, 6)
        );

        assertThrows(
                CasillaVaciaException.class,
                () -> Libro.crear(titulo, idAutor,null, descripcion, paginas, precio, stock, "Primera edición", idEditorial, fecha, CategoriaLibro.NOVELA, "imagen.jpg", EstadoLibro.DISPONIBLE
                )
        );
    }

    @Test
    void noDebePermitirCambiarElPrecioPorUnoNulo() {

        Libro libro = crearLibro();
        Precio precioOriginal = libro.getPrecio();

        assertThrows(
                CasillaVaciaException.class,
                () -> libro.cambiarPrecio(null)
        );
        assertEquals(precioOriginal, libro.getPrecio());
    }

    @Test
    void noDebePermitirCambiarElEstadoPorUnoNulo() {

        Libro libro = crearLibro();
        EstadoLibro estadoOriginal = libro.getEstado();

        assertThrows(
                CasillaVaciaException.class,
                () -> libro.cambiarEstado(null)
        );
        assertEquals(estadoOriginal, libro.getEstado());
    }

    private Libro crearLibro() {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN("9783161484100"), "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP), new Stock(10), "Primera edición", UUID.randomUUID(), new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA, "imagen.jpg", EstadoLibro.DISPONIBLE);
    }
}
