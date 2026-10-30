package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.exception.StockInsuficienteException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

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
    @Test
    void crear_debeIniciarNoEliminado() {
        assertFalse(libro(10, EstadoLibro.DISPONIBLE).isEliminado());
    }

    @Test
    void eliminarLogicamente_marcaElLibroComoEliminado() {
        Libro libro = libro(10, EstadoLibro.DISPONIBLE);

        libro.eliminarLogicamente();

        assertTrue(libro.isEliminado());
    }

    @Test
    void eliminarLogicamente_siYaEstaEliminado_lanzaReglaDominio() {
        Libro libro = libro(10, EstadoLibro.DISPONIBLE);
        libro.eliminarLogicamente();

        assertThrows(ReglaDominioException.class, libro::eliminarLogicamente);
    }

    @Test
    void reconstruir_conservaLaMarcaDeEliminado() {
        Libro libro = Libro.reconstruir(UUID.randomUUID(), new NombreLibro("El Principito"), "autor-123",
                new ISBN("9783161484100"), "Descripción", new NumeroPaginas(120),
                new Precio(25000, Moneda.COP), new Stock(10), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA, "imagen.jpg",
                EstadoLibro.DISPONIBLE, true);

        assertTrue(libro.isEliminado());
    }

    @Test
    void libroEliminado_noPermiteModificaciones() {
        Libro libro = libro(10, EstadoLibro.DISPONIBLE);
        libro.eliminarLogicamente();

        assertAll(
                () -> assertThrows(ReglaDominioException.class, () -> libro.cambiarTitulo(new NombreLibro("Otro"))),
                () -> assertThrows(ReglaDominioException.class, () -> libro.cambiarPrecio(new Precio(1, Moneda.USD))),
                () -> assertThrows(ReglaDominioException.class, () -> libro.actualizarStock(new Stock(3))),
                () -> assertThrows(ReglaDominioException.class, () -> libro.cambiarEstado(EstadoLibro.AGOTADO)),
                () -> assertThrows(ReglaDominioException.class, () -> libro.descontarStock(1)),
                () -> assertThrows(ReglaDominioException.class, () -> libro.reponerStock(1))
        );
    }

    @Test
    void descontarStock_reduceElStock() {
        Libro libro = libro(10, EstadoLibro.DISPONIBLE);

        libro.descontarStock(4);

        assertEquals(new Stock(6), libro.getStock());
        assertEquals(EstadoLibro.DISPONIBLE, libro.getEstado());
    }

    @Test
    void descontarStock_hastaCero_pasaAAgotado() {
        Libro libro = libro(5, EstadoLibro.DISPONIBLE);

        libro.descontarStock(5);

        assertEquals(new Stock(0), libro.getStock());
        assertEquals(EstadoLibro.AGOTADO, libro.getEstado());
    }

    @Test
    void descontarStock_mayorAlStock_lanzaYNoCambiaNada() {
        Libro libro = libro(5, EstadoLibro.DISPONIBLE);

        assertThrows(StockInsuficienteException.class, () -> libro.descontarStock(6));

        assertEquals(new Stock(5), libro.getStock());
        assertEquals(EstadoLibro.DISPONIBLE, libro.getEstado());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -3})
    void descontarStock_cantidadNoPositiva_lanza(int cantidad) {
        Libro libro = libro(5, EstadoLibro.DISPONIBLE);

        assertThrows(ReglaDominioException.class, () -> libro.descontarStock(cantidad));
        assertEquals(new Stock(5), libro.getStock());
    }

    @Test
    void reponerStock_sumaAlStock() {
        Libro libro = libro(5, EstadoLibro.DISPONIBLE);

        libro.reponerStock(3);

        assertEquals(new Stock(8), libro.getStock());
    }

    @Test
    void reponerStock_siEstabaAgotado_vuelveADisponible() {
        Libro libro = libro(0, EstadoLibro.AGOTADO);

        libro.reponerStock(2);

        assertEquals(new Stock(2), libro.getStock());
        assertEquals(EstadoLibro.DISPONIBLE, libro.getEstado());
    }

    @Test
    void reponerStock_siEstaDescontinuado_noCambiaElEstado() {
        Libro libro = libro(0, EstadoLibro.DESCONTINUADO);

        libro.reponerStock(2);

        assertEquals(EstadoLibro.DESCONTINUADO, libro.getEstado());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void reponerStock_cantidadNoPositiva_lanza(int cantidad) {
        Libro libro = libro(5, EstadoLibro.DISPONIBLE);

        assertThrows(ReglaDominioException.class, () -> libro.reponerStock(cantidad));
        assertEquals(new Stock(5), libro.getStock());
    }

    @Test
    void cambiarEstado_desdeDescontinuado_lanza() {
        Libro libro = libro(5, EstadoLibro.DESCONTINUADO);

        assertThrows(ReglaDominioException.class, () -> libro.cambiarEstado(EstadoLibro.DISPONIBLE));
        assertEquals(EstadoLibro.DESCONTINUADO, libro.getEstado());
    }

    @Test
    void verificarDisponibleParaVenta_siEstaDisponible_noLanza() {
        assertDoesNotThrow(() -> libro(5, EstadoLibro.DISPONIBLE).verificarDisponibleParaVenta());
    }

    @ParameterizedTest
    @EnumSource(value = EstadoLibro.class, names = {"AGOTADO", "DESCONTINUADO"})
    void verificarDisponibleParaVenta_siNoEstaDisponible_lanza(EstadoLibro estado) {
        Libro libro = libro(5, estado);

        assertThrows(ReglaDominioException.class, libro::verificarDisponibleParaVenta);
    }

    @Test
    void verificarDisponibleParaVenta_siEstaEliminado_lanza() {
        Libro libro = libro(5, EstadoLibro.DISPONIBLE);
        libro.eliminarLogicamente();

        assertThrows(ReglaDominioException.class, libro::verificarDisponibleParaVenta);
    }

    @Test
    void crear_sinEdicion_lanzaCasillaVacia() {
        assertThrows(CasillaVaciaException.class, () -> Libro.crear(new NombreLibro("El Principito"), "autor-123",
                new ISBN("9783161484100"), "Descripción", new NumeroPaginas(120), new Precio(25000, Moneda.COP),
                new Stock(10), null, UUID.randomUUID(), new FechaPublicacion(LocalDate.of(1943, 4, 6)),
                CategoriaLibro.NOVELA, "imagen.jpg", EstadoLibro.DISPONIBLE));
    }

    private Libro libro(int stock, EstadoLibro estado) {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN("9783161484100"),
                "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP),
                new Stock(stock), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA, "imagen.jpg", estado);
    }

    private Libro crearLibro() {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN("9783161484100"), "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP), new Stock(10), "Primera edición", UUID.randomUUID(), new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA, "imagen.jpg", EstadoLibro.DISPONIBLE);
    }
}
