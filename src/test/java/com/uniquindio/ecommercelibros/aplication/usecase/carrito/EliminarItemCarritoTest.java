package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
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
class EliminarItemCarritoTest {

    private static final ISBN ISBN_A = new ISBN("9783161484100");
    private static final ISBN ISBN_B = new ISBN("9789587580188");

    @Mock private CarritoRepository carritoRepository;
    @InjectMocks private com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarItemCarrito eliminarItem;

    @Test
    void ejecutar_libroEnElCarrito_loQuitaYGuarda() {
        Carrito carrito = Carrito.crear("u1");
        carrito.agregarItem(ISBN_A, 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.agregarItem(ISBN_B, 1, new Precio(20000, Moneda.COP), new Stock(10));
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        eliminarItem.ejecutar(carrito.getIdCarrito(), ISBN_A);

        assertFalse(carrito.contieneLibro(ISBN_A));
        assertTrue(carrito.contieneLibro(ISBN_B));
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void ejecutar_carritoInexistente_lanzaNotFoundYNoGuarda() {
        UUID id = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> eliminarItem.ejecutar(id, ISBN_A));
        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void ejecutar_libroQueNoEstaEnElCarrito_lanzaYNoGuarda() {
        Carrito carrito = Carrito.crear("u1");
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        assertThrows(RecursoNoEncontradoException.class, () -> eliminarItem.ejecutar(carrito.getIdCarrito(), ISBN_A));
        verify(carritoRepository, never()).guardarCarrito(any());
    }
}
