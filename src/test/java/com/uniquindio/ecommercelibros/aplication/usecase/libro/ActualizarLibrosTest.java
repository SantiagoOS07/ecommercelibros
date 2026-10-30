package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
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

    @Mock private LibroRepository libroRepository;
    @Mock private VerificadorDeCarrito verificadorDeCarrito;
    @InjectMocks private ActualizarLibro actualizarLibro;

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

    private static Libro libro() {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN("9783161484100"),
                "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP),
                new Stock(10), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA,
                "imagen.jpg", EstadoLibro.DISPONIBLE);
    }
}