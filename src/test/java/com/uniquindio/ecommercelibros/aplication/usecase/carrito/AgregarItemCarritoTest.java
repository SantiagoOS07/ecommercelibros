package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.AgregarItemCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
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

class AgregarItemCarritoTest {

    private CarritoRepository carritoRepository;
    private LibroRepository libroRepository;
    private AgregarItemCarrito agregarItemCarrito;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        libroRepository = mock(LibroRepository.class);
        agregarItemCarrito = new AgregarItemCarrito(carritoRepository, libroRepository);
    }

    private Libro libroMock(UUID idLibro) {
        Libro libro = mock(Libro.class);
        when(libro.getIsbn()).thenReturn(new ISBN("9783161484100"));
        when(libro.getPrecio()).thenReturn(new Precio(20000, Moneda.COP));
        when(libro.getStock()).thenReturn(new Stock(10));
        when(libroRepository.buscarPorIdLibro(idLibro)).thenReturn(Optional.of(libro));
        return libro;
    }

    @Test
    void agregaElItemCargandoElLibroPorId() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        Carrito carrito = Carrito.crear("usuario-123");
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.of(carrito));
        Libro libro = libroMock(idLibro);

        agregarItemCarrito.ejecutar(idCarrito, idLibro, 2);

        assertEquals(1, carrito.getItems().size());
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
        verify(libro).verificarDisponibleParaVenta();
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void falla404SiElCarritoNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> agregarItemCarrito.ejecutar(idCarrito, UUID.randomUUID(), 1));

        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void falla404SiElLibroNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.of(Carrito.crear("usuario-123")));
        when(libroRepository.buscarPorIdLibro(idLibro)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> agregarItemCarrito.ejecutar(idCarrito, idLibro, 1));

        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void noAgregaUnLibroNoDisponibleParaLaVenta() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        Carrito carrito = Carrito.crear("usuario-123");
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.of(carrito));
        Libro libro = libroMock(idLibro);
        doThrow(new ReglaDominioException("no disponible")).when(libro).verificarDisponibleParaVenta();

        assertThrows(ReglaDominioException.class,
                () -> agregarItemCarrito.ejecutar(idCarrito, idLibro, 1));

        assertTrue(carrito.getItems().isEmpty());
        verify(carritoRepository, never()).guardarCarrito(any());
    }
}