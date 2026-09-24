package com.uniquindio.ecommercelibros.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoCarritoTest {

    @Test
    void debeExistirEstadoCreado() {
        assertNotNull(EstadoCarrito.CREADO);
    }

    @Test
    void debeExistirEstadoProcesando() {
        assertNotNull(EstadoCarrito.PROCESANDO);
    }

    @Test
    void debeExistirEstadoVendido() {
        assertNotNull(EstadoCarrito.VENDIDO);
    }

    @Test
    void creadoPuedeTransicionarAProcesando() {
        assertTrue(
                EstadoCarrito.CREADO
                        .puedeTransicionarA(
                                EstadoCarrito.PROCESANDO
                        )
        );
    }

    @Test
    void creadoNoPuedeTransicionarAVendido() {
        assertFalse(
                EstadoCarrito.CREADO
                        .puedeTransicionarA(
                                EstadoCarrito.VENDIDO
                        )
        );
    }

    @Test
    void creadoNoPuedePermanecerEnCreado() {
        assertFalse(
                EstadoCarrito.CREADO
                        .puedeTransicionarA(
                                EstadoCarrito.CREADO
                        )
        );
    }

    @Test
    void procesandoPuedeTransicionarAVendido() {
        assertTrue(
                EstadoCarrito.PROCESANDO
                        .puedeTransicionarA(
                                EstadoCarrito.VENDIDO
                        )
        );
    }

    @Test
    void procesandoNoPuedeRegresarACreado() {
        assertFalse(
                EstadoCarrito.PROCESANDO
                        .puedeTransicionarA(
                                EstadoCarrito.CREADO
                        )
        );
    }

    @Test
    void procesandoNoPuedePermanecerEnProcesando() {
        assertFalse(
                EstadoCarrito.PROCESANDO
                        .puedeTransicionarA(
                                EstadoCarrito.PROCESANDO
                        )
        );
    }

    @Test
    void vendidoNoPuedeTransicionarACreado() {
        assertFalse(
                EstadoCarrito.VENDIDO
                        .puedeTransicionarA(
                                EstadoCarrito.CREADO
                        )
        );
    }

    @Test
    void vendidoNoPuedeTransicionarAProcesando() {
        assertFalse(
                EstadoCarrito.VENDIDO
                        .puedeTransicionarA(
                                EstadoCarrito.PROCESANDO
                        )
        );
    }

    @Test
    void vendidoNoPuedeTransicionarASiMismo() {
        assertFalse(
                EstadoCarrito.VENDIDO
                        .puedeTransicionarA(
                                EstadoCarrito.VENDIDO
                        )
        );
    }

    @Test
    void vendidoDebeSerEstadoFinal() {
        assertTrue(
                EstadoCarrito.VENDIDO.esFinal()
        );
    }

    @Test
    void creadoNoDebeSerEstadoFinal() {
        assertFalse(
                EstadoCarrito.CREADO.esFinal()
        );
    }

    @Test
    void procesandoNoDebeSerEstadoFinal() {
        assertFalse(
                EstadoCarrito.PROCESANDO.esFinal()
        );
    }

    @Test
    void debeTenerTresEstados() {
        assertEquals(
                3,
                EstadoCarrito.values().length
        );
    }
}