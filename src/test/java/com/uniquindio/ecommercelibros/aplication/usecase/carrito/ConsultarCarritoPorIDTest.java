package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarCarritoPorIDTest {

    @Mock private CarritoRepository carritoRepository;
    @InjectMocks private com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConsultarCarritoPorID consultar;

    @Test
    void ejecutar_carritoExistente_loDevuelve() {
        Carrito carrito = Carrito.crear("u1");
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        assertEquals(Optional.of(carrito), consultar.ejecutar(carrito.getIdCarrito()));
    }

    @Test
    void ejecutar_carritoInexistente_devuelveVacio() {
        UUID id = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(id)).thenReturn(Optional.empty());

        assertTrue(consultar.ejecutar(id).isEmpty());
    }
}
