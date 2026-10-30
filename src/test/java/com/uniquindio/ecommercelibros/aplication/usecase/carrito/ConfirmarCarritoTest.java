package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmarCarritoTest {

    @Mock private CarritoRepository carritoRepository;
    @InjectMocks private com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConfirmarCarrito confirmarCarrito;

    @Test
    void ejecutar_carritoConItems_loPasaAProcesandoYGuarda() {
        Carrito carrito = Carrito.crear("u1");
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        confirmarCarrito.ejecutar(carrito.getIdCarrito());

        assertEquals(EstadoCarrito.PROCESANDO, carrito.getEstado());
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void ejecutar_carritoYaProcesando_lanzaYNoGuarda() {
        Carrito carrito = Carrito.crear("u1");
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        assertThrows(ReglaDominioException.class, () -> confirmarCarrito.ejecutar(carrito.getIdCarrito()));
        verify(carritoRepository, never()).guardarCarrito(any());
    }
}
