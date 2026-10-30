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
class EliminarCarritoTest {

    @Mock private CarritoRepository carritoRepository;
    @InjectMocks private com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarCarrito eliminarCarrito;

    @Test
    void ejecutar_carritoCreado_loEliminaLogicamenteYGuarda() {
        Carrito carrito = Carrito.crear("u1");
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        eliminarCarrito.ejecutar(carrito.getIdCarrito());

        assertTrue(carrito.isEliminado());
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void ejecutar_carritoInexistente_lanzaNotFoundYNoGuarda() {
        UUID id = UUID.randomUUID();
        when(carritoRepository.buscarPorIdCarrito(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> eliminarCarrito.ejecutar(id));
        verify(carritoRepository, never()).guardarCarrito(any());
    }

    @Test
    void ejecutar_carritoProcesando_lanzaYNoLoMarca() {
        Carrito carrito = Carrito.crear("u1");
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));

        assertThrows(ReglaDominioException.class, () -> eliminarCarrito.ejecutar(carrito.getIdCarrito()));
        assertFalse(carrito.isEliminado());
        verify(carritoRepository, never()).guardarCarrito(any());
    }
}