package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCarritoTest {

    @Test
    void dosItemsConDatosIdenticosDebenSerEntidadesDiferentes() {
        ItemCarrito item1 = crearItem();
        ItemCarrito item2 = crearItem();

        boolean sonDiferentes = !item1.equals(item2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearItemCarritoConIsbnNulo() {
        assertThrows(ReglaDominioException.class, () ->
                ItemCarrito.crear(null, 1, new Precio(20000, Moneda.COP), new Stock(10)));
    }

    @Test
    void noDebePermitirCrearItemCarritoConCantidadCeroONegativa() {
        assertThrows(ReglaDominioException.class, () ->
                ItemCarrito.crear(new ISBN("9783161484100"), 0, new Precio(20000, Moneda.COP), new Stock(10)));
    }

    @Test
    void noDebePermitirCrearItemCarritoConPrecioNulo() {
        assertThrows(ReglaDominioException.class, () ->
                ItemCarrito.crear(new ISBN("9783161484100"), 1, null, new Stock(10)));
    }

    @Test
    void noDebePermitirCrearItemCarritoConStockNulo() {
        assertThrows(ReglaDominioException.class, () ->
                ItemCarrito.crear(new ISBN("9783161484100"), 1, new Precio(20000, Moneda.COP), null));
    }

    @Test
    void noDebePermitirCrearItemCarritoConCantidadMayorAlStockDisponible() {
        assertThrows(ReglaDominioException.class, () ->
                ItemCarrito.crear(new ISBN("9783161484100"), 20, new Precio(20000, Moneda.COP), new Stock(5)));
    }

    @Test
    void debeCrearItemCarritoCorrectamenteYCalcularPrecioTotal() {
        ItemCarrito item = crearItem();

        assertEquals(2, item.getCantidad());
        assertEquals(new Precio(40000, Moneda.COP), item.getPrecioTotal());
    }

    @Test
    void debeActualizarCantidadYRecalcularPrecioTotalCorrectamente() {
        ItemCarrito item = crearItem();

        item.actualizarCantidad(4, new Stock(10));

        assertEquals(4, item.getCantidad());
        assertEquals(new Precio(80000, Moneda.COP), item.getPrecioTotal());
    }

    @Test
    void noDebePermitirActualizarCantidadPorEncimaDelStockRecibido() {
        ItemCarrito item = crearItem();

        assertThrows(ReglaDominioException.class, () -> item.actualizarCantidad(50, new Stock(10)));
        assertEquals(2, item.getCantidad());
        assertEquals(new Precio(40000, Moneda.COP), item.getPrecioTotal());
    }

    @Test
    void debeValidarCantidadContraElStockRecibidoEnLaActualizacionNoContraElStockDeCreacion() {
        ItemCarrito item = crearItem();

        assertThrows(ReglaDominioException.class, () -> item.actualizarCantidad(5, new Stock(3)));
        assertEquals(2, item.getCantidad());
    }

    @Test
    void debeActualizarPrecioCorrectamente() {
        ItemCarrito item = crearItem();

        item.actualizarPrecio(new Precio(30000, Moneda.COP));

        assertEquals(new Precio(60000, Moneda.COP), item.getPrecioTotal());
    }

    @Test
    void noDebePermitirActualizarPrecioConUnValorNulo() {
        ItemCarrito item = crearItem();
        Precio precioTotalOriginal = item.getPrecioTotal();

        assertThrows(ReglaDominioException.class, () -> item.actualizarPrecio(null));
        assertEquals(precioTotalOriginal, item.getPrecioTotal());
    }

    private ItemCarrito crearItem() {
        return ItemCarrito.crear(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));
    }
}
