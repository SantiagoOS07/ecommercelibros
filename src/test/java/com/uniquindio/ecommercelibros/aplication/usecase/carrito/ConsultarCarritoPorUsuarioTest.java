package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConsultarCarritoPorUsuario;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultarCarritoPorUsuarioTest {

    private CarritoRepository carritoRepository;
    private ConsultarCarritoPorUsuario consultarCarrito;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        consultarCarrito =
                new ConsultarCarritoPorUsuario(carritoRepository);
    }

    @Test
    void consultarCarritoDeUsuarioExistente() {
        String idUsuario = "usuario-123";
        Carrito carrito = Carrito.crear(idUsuario);

        when(carritoRepository.buscarPorIdUsuario(idUsuario))
                .thenReturn(Optional.of(carrito));

        Optional<Carrito> resultado =
                consultarCarrito.ejecutar(idUsuario);

        assertTrue(resultado.isPresent());
        assertEquals(carrito, resultado.get());

        verify(carritoRepository).buscarPorIdUsuario(idUsuario);
    }

    @Test
    void consultarCarritoDeUsuarioSinCarrito() {
        String idUsuario = "usuario-sin-carrito";

        when(carritoRepository.buscarPorIdUsuario(idUsuario))
                .thenReturn(Optional.empty());

        Optional<Carrito> resultado =
                consultarCarrito.ejecutar(idUsuario);

        assertTrue(resultado.isEmpty());

        verify(carritoRepository).buscarPorIdUsuario(idUsuario);
    }
}

