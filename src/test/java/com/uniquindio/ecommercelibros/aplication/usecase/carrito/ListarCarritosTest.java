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
class ListarCarritosTest {

    @Mock private CarritoRepository carritoRepository;
    @InjectMocks private com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ListarCarritos listar;

    @Test
    void ejecutar_devuelveLosCarritosDelRepositorio() {
        List<Carrito> carritos = List.of(Carrito.crear("u1"), Carrito.crear("u2"));
        when(carritoRepository.listarTodos()).thenReturn(carritos);

        assertEquals(carritos, listar.ejecutar());
    }

    @Test
    void ejecutar_sinCarritos_devuelveListaVacia() {
        when(carritoRepository.listarTodos()).thenReturn(List.of());

        assertTrue(listar.ejecutar().isEmpty());
    }
}
