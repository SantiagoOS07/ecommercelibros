package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.AgregarItemCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgregarItemCarritoTest {

    private CarritoRepository carritoRepository;
    private AgregarItemCarrito agregarItemCarrito;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        agregarItemCarrito = new AgregarItemCarrito(carritoRepository);
    }

    @Test
    void agregarItemCuandoCarritoExiste() {
        UUID idCarrito = UUID.randomUUID();
        Carrito carrito = Carrito.crear("usuario-123");
        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.of(carrito));

        agregarItemCarrito.ejecutar(idCarrito, new ISBN("9783161484100"), 1,
                new Precio(20000, Moneda.COP), new Stock(10));

        assertEquals(1, carrito.getItems().size());
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void agregarItemCuandoCarritoNoExiste() {
        UUID idCarrito = UUID.randomUUID();

        when(carritoRepository.buscarPorIdCarrito(idCarrito))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () ->
                agregarItemCarrito.ejecutar(
                        idCarrito,
                        mock(ISBN.class),
                        1,
                        mock(Precio.class),
                        mock(Stock.class)
                )
        );

        verify(carritoRepository, never()).guardarCarrito(any());
    }
}

