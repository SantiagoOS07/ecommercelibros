package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ActualizarCantidadItemCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActualizarCantidadItemCarritoTest {

    private CarritoRepository carritoRepository;
    private ActualizarCantidadItemCarrito actualizarCantidad;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        actualizarCantidad =
                new ActualizarCantidadItemCarrito(carritoRepository);
    }

    @Test
    void actualizarCantidadCuandoCarritoExiste() {
        UUID idCarrito = UUID.randomUUID();
        Carrito carrito = Carrito.crear("usuario-123");

        ISBN isbn = mock(ISBN.class);
        Stock stock = mock(Stock.class);

        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.of(carrito));
        assertDoesNotThrow(() ->
                actualizarCantidad.ejecutar(
                        idCarrito, isbn, 1, stock
                )
        );

        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void actualizarCantidadCuandoCarritoNoExiste() {
        UUID idCarrito = UUID.randomUUID();

        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () ->
                actualizarCantidad.ejecutar(
                        idCarrito,
                        mock(ISBN.class),
                        1,
                        mock(Stock.class)
                )
        );

        verify(carritoRepository, never()).guardarCarrito(any());
    }
}

