package com.uniquindio.ecommercelibros.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoUsuarioTest {

    @Test
    void debeTenerDosEstados() {
        assertEquals(
                2,
                EstadoUsuario.values().length
        );
    }

    @Test
    void activoPuedePasarAInactivo() {
        assertTrue(
                EstadoUsuario.ACTIVO
                        .puedeTransicionarA(
                                EstadoUsuario.INACTIVO
                        )
        );
    }

    @Test
    void activoNoPuedePermanecerActivo() {
        assertFalse(
                EstadoUsuario.ACTIVO
                        .puedeTransicionarA(
                                EstadoUsuario.ACTIVO
                        )
        );
    }

    @Test
    void inactivoPuedePasarAActivo() {
        assertTrue(
                EstadoUsuario.INACTIVO
                        .puedeTransicionarA(
                                EstadoUsuario.ACTIVO
                        )
        );
    }

    @Test
    void inactivoNoPuedePermanecerInactivo() {
        assertFalse(
                EstadoUsuario.INACTIVO
                        .puedeTransicionarA(
                                EstadoUsuario.INACTIVO
                        )
        );
    }

    @Test
    void activoDebeSerIdentificadoComoActivo() {
        assertTrue(
                EstadoUsuario.ACTIVO.estaActivo()
        );

        assertFalse(
                EstadoUsuario.ACTIVO.estaInactivo()
        );
    }

    @Test
    void inactivoDebeSerIdentificadoComoInactivo() {
        assertFalse(
                EstadoUsuario.INACTIVO.estaActivo()
        );

        assertTrue(
                EstadoUsuario.INACTIVO.estaInactivo()
        );
    }
}