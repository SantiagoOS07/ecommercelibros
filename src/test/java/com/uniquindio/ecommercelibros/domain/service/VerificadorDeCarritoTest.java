package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VerificadorDeCarritoTest {

    private static final String ISBN_A = "9783161484100";
    private static final String ISBN_B = "9789587580188";

    private CarritoRepository carritoRepository;
    private VerificadorDeCarrito verificador;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        verificador = new VerificadorDeCarrito(carritoRepository);
    }

    @Test
    void debeActualizarYGuardarElStockDeLosItemsDelLibro() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN(ISBN_A), 2, new Precio(20000, Moneda.COP), new Stock(10));
        when(carritoRepository.listarCarritosEnCreado()).thenReturn(List.of(carrito));

        verificador.actualizarStockItemsCarritoCreado(ISBN_A, new Stock(3));

        assertEquals(new Stock(3), carrito.getItems().getFirst().getStockLibro());
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void debeActualizarPrecioRecalcularTotalYGuardar() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN(ISBN_A), 2, new Precio(20000, Moneda.COP), new Stock(10));
        when(carritoRepository.listarCarritosEnCreado()).thenReturn(List.of(carrito));

        verificador.actualizarPrecioItemsCarritoCreado(ISBN_A, new Precio(25000, Moneda.COP));

        assertEquals(new Precio(50000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
        assertEquals(50000.0 / 3200, carrito.getPrecioTotal().monto(), 0.0001);
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void noDebeGuardarCarritosQueNoContienenElLibro() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN(ISBN_B), 1, new Precio(20000, Moneda.COP), new Stock(10));
        when(carritoRepository.listarCarritosEnCreado()).thenReturn(List.of(carrito));

        verificador.actualizarStockItemsCarritoCreado(ISBN_A, new Stock(3));

        verify(carritoRepository, never()).guardarCarrito(any());
    }
}
