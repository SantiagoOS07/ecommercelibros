package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarItemCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
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

class EliminarItemCarritoTest {

    private static final ISBN ISBN_LIBRO = new ISBN("9783161484100");

    private CarritoRepository carritoRepository;
    private LibroRepository libroRepository;
    private EliminarItemCarrito eliminarItem;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        libroRepository = mock(LibroRepository.class);
        eliminarItem = new EliminarItemCarrito(carritoRepository, libroRepository);
    }

    private void libroExiste(UUID idLibro) {
        Libro libro = mock(Libro.class);
        when(libro.getIsbn()).thenReturn(ISBN_LIBRO);
        when(libroRepository.buscarPorIdLibro(idLibro)).thenReturn(Optional.of(libro));
    }

    @Test
    void eliminaElItemDelCarrito() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        Carrito carrito = Carrito.crear("usuario-123");
        carrito.agregarItem(ISBN_LIBRO, 1, new Precio(20000, Moneda.COP), new Stock(10));
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.of(carrito));
        libroExiste(idLibro);

        eliminarItem.ejecutar(idCarrito, idLibro);

        assertTrue(carrito.getItems().isEmpty());
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void falla404SiElLibroNoEstaEnElCarrito() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.of(Carrito.crear("usuario-123")));
        libroExiste(idLibro);

        assertThrows(RecursoNoEncontradoException.class,
                () -> eliminarItem.ejecutar(idCarrito, idLibro));

        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void falla404SiElCarritoNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> eliminarItem.ejecutar(idCarrito, UUID.randomUUID()));
    }

    @Test
    void falla404SiElLibroNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.of(Carrito.crear("usuario-123")));
        when(libroRepository.buscarPorIdLibro(idLibro)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> eliminarItem.ejecutar(idCarrito, idLibro));
    }
}