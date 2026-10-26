package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConfirmarCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfirmarCarritoErroresTest {

    private CarritoRepository carritoRepository;
    private ConfirmarCarrito confirmarCarrito;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        confirmarCarrito = new ConfirmarCarrito(carritoRepository);
    }

    @Test
    void noDeberiaGuardarSiElCarritoNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(idCarrito)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> confirmarCarrito.ejecutar(idCarrito));

        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void noDeberiaGuardarSiElCarritoEstaVacio() {
        Carrito carritoVacio = Carrito.crear("usuario-123");
        when(carritoRepository.buscarPorIdCarrito(carritoVacio.getIdCarrito()))
                .thenReturn(Optional.of(carritoVacio));

        assertThrows(ReglaDominioException.class,
                () -> confirmarCarrito.ejecutar(carritoVacio.getIdCarrito()));

        verify(carritoRepository, never()).guardarCarrito(any());
    }
}