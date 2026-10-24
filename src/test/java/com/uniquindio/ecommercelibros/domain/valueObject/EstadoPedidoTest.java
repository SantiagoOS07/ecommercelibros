package com.uniquindio.ecommercelibros.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPedidoTest {

    @Test
    void debeTenerCincoEstados() {
        assertEquals(
                5,
                EstadoPedido.values().length
        );
    }

    @Test
    void enviadoPuedePasarAEntregado() {
        assertTrue(
                EstadoPedido.ENVIADO
                        .puedeTransicionarA(
                                EstadoPedido.ENTREGADO
                        )
        );
    }

    @Test
    void enviadoNoPuedeSerCancelado() {
        assertFalse(
                EstadoPedido.ENVIADO
                        .puedeTransicionarA(
                                EstadoPedido.CANCELADO
                        )
        );
    }

    @Test
    void entregadoNoPuedeTransicionarAEnProceso() {
        assertFalse(
                EstadoPedido.ENTREGADO
                        .puedeTransicionarA(
                                EstadoPedido.PREPARANDO
                        )
        );
    }

    @Test
    void entregadoNoPuedeTransicionarASiMismo() {
        assertFalse(
                EstadoPedido.ENTREGADO
                        .puedeTransicionarA(
                                EstadoPedido.ENTREGADO
                        )
        );
    }

    @Test
    void canceladoNoPuedeTransicionarAEnProceso() {
        assertFalse(
                EstadoPedido.CANCELADO
                        .puedeTransicionarA(
                                EstadoPedido.PREPARANDO
                        )
        );
    }

    @Test
    void canceladoNoPuedeTransicionarASiMismo() {
        assertFalse(
                EstadoPedido.CANCELADO
                        .puedeTransicionarA(
                                EstadoPedido.CANCELADO
                        )
        );
    }

    @Test
    void entregadoEsEstadoFinal() {
        assertTrue(
                EstadoPedido.ENTREGADO.esFinal()
        );
    }

    @Test
    void canceladoEsEstadoFinal() {
        assertTrue(
                EstadoPedido.CANCELADO.esFinal()
        );
    }

    @Test
    void enProcesoNoEsEstadoFinal() {
        assertFalse(
                EstadoPedido.PREPARANDO.esFinal()
        );
    }

    @Test
    void enviadoNoEsEstadoFinal() {
        assertFalse(
                EstadoPedido.ENVIADO.esFinal()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoPreparandp() {
        assertTrue(
                EstadoPedido.PREPARANDO.estaEnProceso()
        );

        assertFalse(
                EstadoPedido.PREPARANDO.fueReembolsado()
        );

        assertFalse(
                EstadoPedido.PREPARANDO.fueEnviado()
        );

        assertFalse(
                EstadoPedido.PREPARANDO.fueEntregado()
        );

        assertFalse(
                EstadoPedido.PREPARANDO.fueCancelado()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoEsperandoPago() {
        assertFalse(
                EstadoPedido.PREPARANDO.fueEnviado()
        );

        assertTrue(
                EstadoPedido.PREPARANDO.estaEnProceso()
        );

        assertFalse(
                EstadoPedido.PREPARANDO.fueEntregado()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoEnviado() {
        assertFalse(
                EstadoPedido.ENVIADO.estaEnProceso()
        );

        assertFalse(
                EstadoPedido.ENVIADO.fueCancelado()
        );

        assertTrue(
                EstadoPedido.ENVIADO.fueEnviado()
        );

        assertFalse(
                EstadoPedido.ENVIADO.fueEntregado()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoEntregado() {
        assertTrue(
                EstadoPedido.ENTREGADO.fueEntregado()
        );

        assertFalse(
                EstadoPedido.ENTREGADO.fueCancelado()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoCancelado() {
        assertTrue(
                EstadoPedido.CANCELADO.fueCancelado()
        );

        assertFalse(
                EstadoPedido.CANCELADO.fueEntregado()
        );
    }
}