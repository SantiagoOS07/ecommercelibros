package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarLibroTest {

    @Mock
    private LibroRepository libroRepository;
    @Mock
    private VerificadorDeCarrito verificadorDeCarrito;
    @InjectMocks
    private ActualizarLibro actualizarLibro;

    @Test
    void actualizarPrecio_cambiaElPrecioLoPropagaALosCarritosCreadoYGuarda() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));
        Precio nuevo = new Precio(30000, Moneda.COP);

        actualizarLibro.actualizarPrecio(libro.getIdLibro(), nuevo);

        assertEquals(nuevo, libro.getPrecio());
        verify(verificadorDeCarrito).actualizarPrecioItemsCarritoCreado("9783161484100", nuevo);
        verify(libroRepository).guardarLibro(libro);
    }

    @Test
    void actualizarStock_cambiaElStockLoPropagaALosCarritosCreadoYGuarda() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));
        Stock nuevo = new Stock(3);

        actualizarLibro.actualizarStock(libro.getIdLibro(), nuevo);

        assertEquals(nuevo, libro.getStock());
        verify(verificadorDeCarrito).actualizarStockItemsCarritoCreado("9783161484100", nuevo);
        verify(libroRepository).guardarLibro(libro);
    }

    @Test
    void actualizarPrecio_libroInexistente_lanzaNotFoundYNoPropaga() {
        UUID id = UUID.randomUUID();
        when(libroRepository.buscarPorIdLibro(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> actualizarLibro.actualizarPrecio(id, new Precio(1, Moneda.USD)));

        verifyNoInteractions(verificadorDeCarrito);
        verify(libroRepository, never()).guardarLibro(any());
    }

    @Test
    void aplicarDescuento_aplicaElDescuentoActualizaLosCarritosCreadoYGuarda() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));
        Descuento descuento = descuentoVigente(20);

        actualizarLibro.aplicarDescuento(libro.getIdLibro(), descuento);

        assertEquals(descuento, libro.getDescuento());
        verify(verificadorDeCarrito).actualizarPrecioItemsCarritoCreado("9783161484100", new Precio(20000, Moneda.COP));
        verify(libroRepository).guardarLibro(libro);
    }

    @Test
    void aplicarDescuento_libroInexistente_lanzaNotFoundYNoPropaga() {
        UUID id = UUID.randomUUID();
        when(libroRepository.buscarPorIdLibro(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> actualizarLibro.aplicarDescuento(id, descuentoVigente(20)));

        verifyNoInteractions(verificadorDeCarrito);
        verify(libroRepository, never()).guardarLibro(any());
    }

    @Test
    void aplicarDescuento_vencido_lanzaReglaDominioYNoGuarda() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));
        Descuento vencido = new Descuento(20, "Vencido",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1));

        assertThrows(ReglaDominioException.class,
                () -> actualizarLibro.aplicarDescuento(libro.getIdLibro(), vencido));

        verifyNoInteractions(verificadorDeCarrito);
        verify(libroRepository, never()).guardarLibro(any());
    }

    @Test
    void quitarDescuento_quitaElDescuentoRestauraElPrecioEnLosCarritosYGuarda() {
        Libro libro = libro();
        libro.aplicarDescuento(descuentoVigente(20));
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));

        actualizarLibro.quitarDescuento(libro.getIdLibro());

        assertNull(libro.getDescuento());
        verify(verificadorDeCarrito).actualizarPrecioItemsCarritoCreado("9783161484100", new Precio(25000, Moneda.COP));
        verify(libroRepository).guardarLibro(libro);
    }

    @Test
    void quitarDescuento_sinDescuento_lanzaReglaDominioYNoGuarda() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));

        assertThrows(ReglaDominioException.class,
                () -> actualizarLibro.quitarDescuento(libro.getIdLibro()));

        verifyNoInteractions(verificadorDeCarrito);
        verify(libroRepository, never()).guardarLibro(any());
    }

    @Test
    void quitarDescuento_libroInexistente_lanzaNotFound() {
        UUID id = UUID.randomUUID();
        when(libroRepository.buscarPorIdLibro(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> actualizarLibro.quitarDescuento(id));

        verify(libroRepository, never()).guardarLibro(any());
    }

    @Test
    void actualizarPrecio_conDescuentoVigente_propagaElPrecioFinalALosCarritos() {
        Libro libro = libro();
        libro.aplicarDescuento(descuentoVigente(20));
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));
        actualizarLibro.actualizarPrecio(libro.getIdLibro(), new Precio(30000, Moneda.COP));
        verify(verificadorDeCarrito).actualizarPrecioItemsCarritoCreado("9783161484100", new Precio(24000, Moneda.COP));
        verify(libroRepository).guardarLibro(libro);
    }


    private static Descuento descuentoVigente(double porcentaje) {
        return new Descuento(porcentaje, "Temporada", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10));
    }

    private static Libro libro() {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN("9783161484100"),
                "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP),
                new Stock(10), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA,
                "imagen.jpg", EstadoLibro.DISPONIBLE);
    }
}