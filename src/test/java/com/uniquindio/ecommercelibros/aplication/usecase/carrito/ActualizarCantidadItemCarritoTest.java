package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ActualizarCantidadItemCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActualizarCantidadItemCarritoTest {

    private CarritoRepository carritoRepository;
    private LibroRepository libroRepository;
    private ActualizarCantidadItemCarrito actualizarCantidad;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        libroRepository = mock(LibroRepository.class);
        actualizarCantidad = new ActualizarCantidadItemCarrito(carritoRepository, libroRepository);
    }

    @Test
    void actualizarCantidadCuandoCarritoExiste() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        ISBN isbn = new ISBN("9783161484100");
        Carrito carrito = Carrito.crear("usuario-123");
        carrito.agregarItem(isbn, 1, new Precio(20000, Moneda.COP), new Stock(10));
        Libro libro = mock(Libro.class);
        when(libro.getIsbn()).thenReturn(isbn);
        when(libro.getStock()).thenReturn(new Stock(10));
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.of(carrito));
        when(libroRepository.buscarPorIdLibro(idLibro)).thenReturn(Optional.of(libro));

        actualizarCantidad.ejecutar(idCarrito, idLibro, 3);

        assertEquals(3, carrito.getItems().getFirst().getCantidad());
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void actualizarCantidadCuandoCarritoNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> actualizarCantidad.ejecutar(idCarrito, UUID.randomUUID(), 1));

        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void actualizarCantidadCuandoLibroNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.of(Carrito.crear("usuario-123")));
        when(libroRepository.buscarPorIdLibro(idLibro)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> actualizarCantidad.ejecutar(idCarrito, idLibro, 1));

        verify(carritoRepository, never()).guardarCarrito(any());
    }
}

