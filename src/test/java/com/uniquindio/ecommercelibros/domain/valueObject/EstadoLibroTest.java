package com.uniquindio.ecommercelibros.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoLibroTest {

    @Test
    void debeTenerTresEstados() {
        assertEquals(
                3,
                EstadoLibro.values().length
        );
    }

    @Test
    void disponiblePuedePasarAAgotado() {
        assertTrue(
                EstadoLibro.DISPONIBLE
                        .puedeTransicionarA(
                                EstadoLibro.AGOTADO
                        )
        );
    }

    @Test
    void disponiblePuedePasarADescontinuado() {
        assertTrue(
                EstadoLibro.DISPONIBLE
                        .puedeTransicionarA(
                                EstadoLibro.DESCONTINUADO
                        )
        );
    }

    @Test
    void disponibleNoPuedePermanecerDisponible() {
        assertFalse(
                EstadoLibro.DISPONIBLE
                        .puedeTransicionarA(
                                EstadoLibro.DISPONIBLE
                        )
        );
    }

    @Test
    void agotadoPuedePasarADisponible() {
        assertTrue(
                EstadoLibro.AGOTADO
                        .puedeTransicionarA(
                                EstadoLibro.DISPONIBLE
                        )
        );
    }

    @Test
    void agotadoPuedePasarADescontinuado() {
        assertTrue(
                EstadoLibro.AGOTADO
                        .puedeTransicionarA(
                                EstadoLibro.DESCONTINUADO
                        )
        );
    }

    @Test
    void agotadoNoPuedePermanecerAgotado() {
        assertFalse(
                EstadoLibro.AGOTADO
                        .puedeTransicionarA(
                                EstadoLibro.AGOTADO
                        )
        );
    }

    @Test
    void descontinuadoNoPuedePasarADisponible() {
        assertFalse(
                EstadoLibro.DESCONTINUADO
                        .puedeTransicionarA(
                                EstadoLibro.DISPONIBLE
                        )
        );
    }

    @Test
    void descontinuadoNoPuedePasarAAgotado() {
        assertFalse(
                EstadoLibro.DESCONTINUADO
                        .puedeTransicionarA(
                                EstadoLibro.AGOTADO
                        )
        );
    }

    @Test
    void descontinuadoNoPuedeTransicionarASiMismo() {
        assertFalse(
                EstadoLibro.DESCONTINUADO
                        .puedeTransicionarA(
                                EstadoLibro.DESCONTINUADO
                        )
        );
    }

    @Test
    void descontinuadoEsEstadoFinal() {
        assertTrue(
                EstadoLibro.DESCONTINUADO.esFinal()
        );
    }

    @Test
    void disponibleNoEsEstadoFinal() {
        assertFalse(
                EstadoLibro.DISPONIBLE.esFinal()
        );
    }

    @Test
    void agotadoNoEsEstadoFinal() {
        assertFalse(
                EstadoLibro.AGOTADO.esFinal()
        );
    }

    @Test
    void disponibleEstaDisponible() {
        assertTrue(
                EstadoLibro.DISPONIBLE.estaDisponible()
        );

        assertFalse(
                EstadoLibro.DISPONIBLE.estaAgotado()
        );

        assertFalse(
                EstadoLibro.DISPONIBLE.estaDescontinuado()
        );
    }

    @Test
    void agotadoEstaAgotado() {
        assertFalse(
                EstadoLibro.AGOTADO.estaDisponible()
        );

        assertTrue(
                EstadoLibro.AGOTADO.estaAgotado()
        );

        assertFalse(
                EstadoLibro.AGOTADO.estaDescontinuado()
        );
    }

    @Test
    void descontinuadoEstaDescontinuado() {
        assertFalse(
                EstadoLibro.DESCONTINUADO.estaDisponible()
        );

        assertFalse(
                EstadoLibro.DESCONTINUADO.estaAgotado()
        );

        assertTrue(
                EstadoLibro.DESCONTINUADO.estaDescontinuado()
        );
    }
}