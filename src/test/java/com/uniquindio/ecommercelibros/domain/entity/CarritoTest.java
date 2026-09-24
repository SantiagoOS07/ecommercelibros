package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarritoTest {

    @Test
    void dosCarritosConElMismoUsuarioDebenSerEntidadesDiferentes() {
        Carrito carrito1 = crearCarrito();
        Carrito carrito2 = crearCarrito();

        boolean sonDiferentes = !carrito1.equals(carrito2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearCarritoConIdUsuarioNulo() {
        assertThrows(ReglaDominioException.class, () -> Carrito.crear(null));
    }

    @Test
    void noDebePermitirCrearCarritoConIdUsuarioVacio() {
        assertThrows(ReglaDominioException.class, () -> Carrito.crear(""));
    }

    @Test
    void debeCrearCarritoEnEstadoCreadoYConListaDeItemsVacia() {
        Carrito carrito = crearCarrito();

        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void debeConfirmarCarritoCorrectamente() {
        Carrito carrito = crearCarrito();

        carrito.confirmarCarrito();

        assertEquals(EstadoCarrito.PROCESANDO, carrito.getEstado());
    }

    @Test
    void noDebePermitirConfirmarUnCarritoQueYaEstaProcesando() {
        Carrito carrito = crearCarrito();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, carrito::confirmarCarrito);
        assertEquals(EstadoCarrito.PROCESANDO, carrito.getEstado());
    }

    @Test
    void noDebePermitirConfirmarUnCarritoYaVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, carrito::confirmarCarrito);
        assertEquals(EstadoCarrito.VENDIDO, carrito.getEstado());
    }


    @Test
    void debeCancelarConfirmacionCorrectamente() {
        Carrito carrito = crearCarrito();
        carrito.confirmarCarrito();

        carrito.cancelarConfirmacionCarrito();

        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
    }

    @Test
    void noDebePermitirCancelarConfirmacionSiElCarritoNoEstaProcesando() {
        Carrito carrito = crearCarrito();

        assertThrows(ReglaDominioException.class, carrito::cancelarConfirmacionCarrito);
        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
    }

    @Test
    void noDebePermitirCancelarConfirmacionDeUnCarritoYaVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, carrito::cancelarConfirmacionCarrito);
        assertEquals(EstadoCarrito.VENDIDO, carrito.getEstado());
    }


    @Test
    void debeCompletarVentaCorrectamente() {
        Carrito carrito = crearCarrito();
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();

        carrito.completarVentaCarrito();

        assertEquals(EstadoCarrito.VENDIDO, carrito.getEstado());
    }

    @Test
    void noDebePermitirCompletarVentaSiElCarritoNoEstaProcesando() {
        Carrito carrito = crearCarrito();
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));

        assertThrows(ReglaDominioException.class, carrito::completarVentaCarrito);
        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
    }

    @Test
    void noDebePermitirCompletarVentaSiElCarritoNoTieneItems() {
        Carrito carrito = crearCarrito();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, carrito::completarVentaCarrito);
        assertEquals(EstadoCarrito.PROCESANDO, carrito.getEstado());
    }

    @Test
    void noDebePermitirCompletarVentaDeUnCarritoYaVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, carrito::completarVentaCarrito);
        assertEquals(EstadoCarrito.VENDIDO, carrito.getEstado());
    }


    @Test
    void debeAgregarItemCorrectamente() {
        Carrito carrito = crearCarrito();

        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));

        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirAgregarItemConCantidadMayorAlStockDisponible() {
        Carrito carrito = crearCarrito();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(new ISBN("9783161484100"), 20, new Precio(20000, Moneda.COP), new Stock(5)));
        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void noDebePermitirAgregarItemSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarrito();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10)));
        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void noDebePermitirAgregarItemSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(new ISBN("9789587580188"), 1, new Precio(20000, Moneda.COP), new Stock(10)));
        assertEquals(1, carrito.getItems().size());
    }


    @Test
    void debeEliminarItemCorrectamente() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        carrito.eliminarItem(isbn);

        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void eliminarItemInexistenteNoDebeAlterarLaListaDeItems() {
        Carrito carrito = crearCarrito();
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(20000, Moneda.COP), new Stock(10));

        carrito.eliminarItem(new ISBN("9789587580188"));

        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirEliminarItemSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () -> carrito.eliminarItem(isbn));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirEliminarItemSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();
        ISBN isbn = new ISBN("9783161484100");

        assertThrows(ReglaDominioException.class, () -> carrito.eliminarItem(isbn));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void debeActualizarCantidadDeItemCorrectamente() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        carrito.actualizarItemCantidad(isbn, 5, new Stock(10));

        assertEquals(5, carrito.getItems().getFirst().getCantidad());
        assertEquals(new Precio(100000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void actualizarCantidadACeroDebeEliminarElItem() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        carrito.actualizarItemCantidad(isbn, 0, new Stock(10));

        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void noDebePermitirActualizarCantidadPorUnValorNegativo() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(isbn, -1, new Stock(10)));
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void noDebePermitirActualizarCantidadPorEncimaDelStockDisponible() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(isbn, 50, new Stock(10)));
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void noDebePermitirActualizarCantidadSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(isbn, 5, new Stock(10)));
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void noDebePermitirActualizarCantidadSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();
        ISBN isbn = new ISBN("9783161484100");

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(isbn, 5, new Stock(10)));
        assertEquals(1, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void debeActualizarPrecioDeItemCorrectamente() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        carrito.actualizarItemPrecio(isbn, new Precio(25000, Moneda.COP));

        assertEquals(new Precio(50000, Moneda.COP), carrito.getItems().get(0).getPrecioTotal());
    }

    @Test
    void actualizarPrecioDeItemInexistenteNoDebeAlterarLaListaDeItems() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));

        carrito.actualizarItemPrecio(new ISBN("9789587580188"), new Precio(99999, Moneda.COP));

        assertEquals(new Precio(40000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void noDebePermitirActualizarPrecioSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarrito();
        ISBN isbn = new ISBN("9783161484100");
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () ->
                carrito.actualizarItemPrecio(isbn, new Precio(25000, Moneda.COP)));
        assertEquals(new Precio(40000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void noDebePermitirActualizarPrecioSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();
        ISBN isbn = new ISBN("9783161484100");

        assertThrows(ReglaDominioException.class, () ->
                carrito.actualizarItemPrecio(isbn, new Precio(25000, Moneda.COP)));
        assertEquals(new Precio(20000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }


    private Carrito crearCarrito() {
        return Carrito.crear("user-1");
    }

    private Carrito crearCarritoVendido() {
        Carrito carrito = crearCarrito();
        carrito.agregarItem(new ISBN("9783161484100"), 1, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();
        carrito.completarVentaCarrito();
        return carrito;
    }
}