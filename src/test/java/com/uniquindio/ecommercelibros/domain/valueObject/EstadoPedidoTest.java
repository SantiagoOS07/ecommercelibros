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
    void enProcesoPuedePasarAEsperandoPago() {
        assertTrue(
                EstadoPedido.EN_PROCESO
                        .puedeTransicionarA(
                                EstadoPedido.ESPERANDO_PAGO
                        )
        );
    }

    @Test
    void enProcesoPuedePasarACancelado() {
        assertTrue(
                EstadoPedido.EN_PROCESO
                        .puedeTransicionarA(
                                EstadoPedido.CANCELADO
                        )
        );
    }

    @Test
    void enProcesoNoPuedePasarDirectamenteAEnviado() {
        assertFalse(
                EstadoPedido.EN_PROCESO
                        .puedeTransicionarA(
                                EstadoPedido.ENVIADO
                        )
        );
    }

    @Test
    void enProcesoNoPuedePasarDirectamenteAEntregado() {
        assertFalse(
                EstadoPedido.EN_PROCESO
                        .puedeTransicionarA(
                                EstadoPedido.ENTREGADO
                        )
        );
    }

    @Test
    void esperandoPagoPuedePasarAEnviado() {
        assertTrue(
                EstadoPedido.ESPERANDO_PAGO
                        .puedeTransicionarA(
                                EstadoPedido.ENVIADO
                        )
        );
    }

    @Test
    void esperandoPagoPuedePasarACancelado() {
        assertTrue(
                EstadoPedido.ESPERANDO_PAGO
                        .puedeTransicionarA(
                                EstadoPedido.CANCELADO
                        )
        );
    }

    @Test
    void esperandoPagoNoPuedeRegresarAEnProceso() {
        assertFalse(
                EstadoPedido.ESPERANDO_PAGO
                        .puedeTransicionarA(
                                EstadoPedido.EN_PROCESO
                        )
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
    void enviadoNoPuedeRegresarAEsperandoPago() {
        assertFalse(
                EstadoPedido.ENVIADO
                        .puedeTransicionarA(
                                EstadoPedido.ESPERANDO_PAGO
                        )
        );
    }

    @Test
    void entregadoNoPuedeTransicionarAEnProceso() {
        assertFalse(
                EstadoPedido.ENTREGADO
                        .puedeTransicionarA(
                                EstadoPedido.EN_PROCESO
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
                                EstadoPedido.EN_PROCESO
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
                EstadoPedido.EN_PROCESO.esFinal()
        );
    }

    @Test
    void esperandoPagoNoEsEstadoFinal() {
        assertFalse(
                EstadoPedido.ESPERANDO_PAGO.esFinal()
        );
    }

    @Test
    void enviadoNoEsEstadoFinal() {
        assertFalse(
                EstadoPedido.ENVIADO.esFinal()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoEnProceso() {
        assertTrue(
                EstadoPedido.EN_PROCESO.estaEnProceso()
        );

        assertFalse(
                EstadoPedido.EN_PROCESO.estaEsperandoPago()
        );

        assertFalse(
                EstadoPedido.EN_PROCESO.fueEnviado()
        );

        assertFalse(
                EstadoPedido.EN_PROCESO.fueEntregado()
        );

        assertFalse(
                EstadoPedido.EN_PROCESO.fueCancelado()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoEsperandoPago() {
        assertFalse(
                EstadoPedido.ESPERANDO_PAGO.estaEnProceso()
        );

        assertTrue(
                EstadoPedido.ESPERANDO_PAGO.estaEsperandoPago()
        );

        assertFalse(
                EstadoPedido.ESPERANDO_PAGO.fueEnviado()
        );
    }

    @Test
    void debeIdentificarCorrectamenteElEstadoEnviado() {
        assertFalse(
                EstadoPedido.ENVIADO.estaEnProceso()
        );

        assertFalse(
                EstadoPedido.ENVIADO.estaEsperandoPago()
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