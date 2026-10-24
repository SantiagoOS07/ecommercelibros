package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrarCarritoTest {

    private CarritoRepository carritoRepository;
    private RegistrarCarrito registrarCarrito;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        registrarCarrito = new RegistrarCarrito(carritoRepository);
    }

    @Test
    void registrarCarritoCorrectamente() {
        String idUsuario = "usuario-123";

        Carrito resultado = registrarCarrito.ejecutar(idUsuario);

        assertNotNull(resultado);
        assertEquals(idUsuario, resultado.getIdUsuario());

        verify(carritoRepository, times(1))
                .guardarCarrito(resultado);
    }
}
