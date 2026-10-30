package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarritoTest {

    private static final ISBN ISBN_A = new ISBN("9783161484100");
    private static final ISBN ISBN_B = new ISBN("9789587580188");

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
        assertFalse(carrito.isEliminado());
    }

    @Test
    void debeConfirmarCarritoCorrectamente() {
        Carrito carrito = crearCarritoConItem();

        carrito.confirmarCarrito();

        assertEquals(EstadoCarrito.PROCESANDO, carrito.getEstado());
    }

    @Test
    void noDebePermitirConfirmarUnCarritoSinItems() {
        Carrito carrito = crearCarrito();

        assertThrows(ReglaDominioException.class, carrito::confirmarCarrito);
        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
    }

    @Test
    void noDebePermitirConfirmarUnCarritoQueYaEstaProcesando() {
        Carrito carrito = crearCarritoConItem();
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
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        carrito.cancelarConfirmacionCarrito();

        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
    }

    @Test
    void despuesDeCancelarConfirmacionDebePermitirEditarElCarrito() {
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();
        carrito.cancelarConfirmacionCarrito();

        carrito.agregarItem(ISBN_B, 1, new Precio(20000, Moneda.COP), new Stock(10));

        assertEquals(2, carrito.getItems().size());
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
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        carrito.completarVentaCarrito();

        assertEquals(EstadoCarrito.VENDIDO, carrito.getEstado());
    }

    @Test
    void noDebePermitirCompletarVentaSiElCarritoNoEstaProcesando() {
        Carrito carrito = crearCarritoConItem();

        assertThrows(ReglaDominioException.class, carrito::completarVentaCarrito);
        assertEquals(EstadoCarrito.CREADO, carrito.getEstado());
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

        carrito.agregarItem(ISBN_A, 2, new Precio(20000, Moneda.COP), new Stock(10));

        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirAgregarElMismoLibroDosVeces() {
        Carrito carrito = crearCarritoConItem();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(ISBN_A, 1, new Precio(20000, Moneda.COP), new Stock(10)));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirAgregarItemConCantidadMayorAlStockDisponible() {
        Carrito carrito = crearCarrito();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(ISBN_A, 20, new Precio(20000, Moneda.COP), new Stock(5)));
        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void noDebePermitirAgregarItemSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(ISBN_B, 2, new Precio(20000, Moneda.COP), new Stock(10)));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirAgregarItemSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(ISBN_B, 1, new Precio(20000, Moneda.COP), new Stock(10)));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void debeEliminarItemCorrectamente() {
        Carrito carrito = crearCarritoConItem();

        carrito.eliminarItem(ISBN_A);

        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void eliminarItemInexistenteNoDebeAlterarLaListaDeItems() {
        Carrito carrito = crearCarritoConItem();

        carrito.eliminarItem(ISBN_B);

        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirEliminarItemSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () -> carrito.eliminarItem(ISBN_A));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void noDebePermitirEliminarItemSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, () -> carrito.eliminarItem(ISBN_A));
        assertEquals(1, carrito.getItems().size());
    }

    @Test
    void debeActualizarCantidadDeItemCorrectamente() {
        Carrito carrito = crearCarritoConItem();

        carrito.actualizarItemCantidad(ISBN_A, 5, new Stock(10));

        assertEquals(5, carrito.getItems().getFirst().getCantidad());
        assertEquals(new Precio(100000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void actualizarCantidadACeroDebeEliminarElItem() {
        Carrito carrito = crearCarritoConItem();

        carrito.actualizarItemCantidad(ISBN_A, 0, new Stock(10));

        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void noDebePermitirActualizarCantidadPorUnValorNegativo() {
        Carrito carrito = crearCarritoConItem();

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(ISBN_A, -1, new Stock(10)));
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void noDebePermitirActualizarCantidadPorEncimaDelStockDisponible() {
        Carrito carrito = crearCarritoConItem();

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(ISBN_A, 50, new Stock(10)));
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void noDebePermitirActualizarCantidadSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(ISBN_A, 5, new Stock(10)));
        assertEquals(2, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void noDebePermitirActualizarCantidadSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, () -> carrito.actualizarItemCantidad(ISBN_A, 5, new Stock(10)));
        assertEquals(1, carrito.getItems().getFirst().getCantidad());
    }

    @Test
    void debeActualizarPrecioDeItemCorrectamente() {
        Carrito carrito = crearCarritoConItem();

        carrito.actualizarItemPrecio(ISBN_A, new Precio(25000, Moneda.COP));

        assertEquals(new Precio(50000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void actualizarPrecioDeItemInexistenteDebeLanzarExcepcionYNoAlterarLosItems() {
        Carrito carrito = crearCarritoConItem();

        assertThrows(ReglaDominioException.class, () ->
                carrito.actualizarItemPrecio(ISBN_B, new Precio(99999, Moneda.COP)));

        assertEquals(new Precio(40000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void noDebePermitirActualizarPrecioSiElCarritoEstaProcesando() {
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, () ->
                carrito.actualizarItemPrecio(ISBN_A, new Precio(25000, Moneda.COP)));
        assertEquals(new Precio(40000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void noDebePermitirActualizarPrecioSiElCarritoYaFueVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, () ->
                carrito.actualizarItemPrecio(ISBN_A, new Precio(25000, Moneda.COP)));
        assertEquals(new Precio(20000, Moneda.COP), carrito.getItems().getFirst().getPrecioTotal());
    }

    @Test
    void debeEliminarLogicamenteUnCarritoEnEstadoCreado() {
        Carrito carrito = crearCarrito();

        carrito.eliminarLogicamente();

        assertTrue(carrito.isEliminado());
    }

    @Test
    void noDebePermitirEliminarLogicamenteUnCarritoQueYaEstaEliminado() {
        Carrito carrito = crearCarrito();
        carrito.eliminarLogicamente();

        assertThrows(ReglaDominioException.class, carrito::eliminarLogicamente);
    }

    @Test
    void noDebePermitirEliminarLogicamenteUnCarritoProcesando() {
        Carrito carrito = crearCarritoConItem();
        carrito.confirmarCarrito();

        assertThrows(ReglaDominioException.class, carrito::eliminarLogicamente);
        assertFalse(carrito.isEliminado());
    }

    @Test
    void noDebePermitirEliminarLogicamenteUnCarritoVendido() {
        Carrito carrito = crearCarritoVendido();

        assertThrows(ReglaDominioException.class, carrito::eliminarLogicamente);
        assertFalse(carrito.isEliminado());
    }

    @Test
    void noDebePermitirModificarUnCarritoEliminado() {
        Carrito carrito = crearCarrito();
        carrito.eliminarLogicamente();

        assertThrows(ReglaDominioException.class, () ->
                carrito.agregarItem(ISBN_A, 1, new Precio(20000, Moneda.COP), new Stock(10)));
        assertTrue(carrito.getItems().isEmpty());
    }

    @Test
    void reconstruirDebeConservarLaMarcaDeEliminado() {
        Carrito carrito = Carrito.reconstruir(
                UUID.randomUUID(), "user-1", EstadoCarrito.CREADO, List.of(), true);

        assertTrue(carrito.isEliminado());
    }

    // ---------- helpers ----------

    private Carrito crearCarrito() {
        return Carrito.crear("user-1");
    }

    private Carrito crearCarritoConItem() {
        Carrito carrito = crearCarrito();
        carrito.agregarItem(ISBN_A, 2, new Precio(20000, Moneda.COP), new Stock(10));
        return carrito;
    }

    private Carrito crearCarritoVendido() {
        Carrito carrito = crearCarrito();
        carrito.agregarItem(ISBN_A, 1, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();
        carrito.completarVentaCarrito();
        return carrito;
    }
}