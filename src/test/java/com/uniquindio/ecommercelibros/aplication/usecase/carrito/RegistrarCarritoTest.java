package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrarCarritoTest {

    private CarritoRepository carritoRepository;
    private VerificadorDeCarrito verificadorDeCarrito;
    private RegistrarCarrito registrarCarrito;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        verificadorDeCarrito = mock(VerificadorDeCarrito.class);
        registrarCarrito = new RegistrarCarrito(carritoRepository, verificadorDeCarrito);
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

    @Test
    void noDebeRegistrarCarritoSiElUsuarioYaTieneUnoActivo() {
        String idUsuario = "usuario-123";

        doThrow(new RecursoDuplicadoException("El usuario ya tiene un carrito activo"))
                .when(verificadorDeCarrito)
                .verificarQueUsuarioNoTieneCarrito(idUsuario);

        assertThrows(RecursoDuplicadoException.class,
                () -> registrarCarrito.ejecutar(idUsuario));

        verify(carritoRepository, never()).guardarCarrito(any());
    }
}
