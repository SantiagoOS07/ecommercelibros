package com.uniquindio.ecommercelibros.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonedaTest {

    @Test
    void debeExistirMonedaCOP() {
        assertNotNull(Moneda.COP);
    }

    @Test
    void debeExistirMonedaUSD() {
        assertNotNull(Moneda.USD);
    }

    @Test
    void COPDebeTenerEquivalencia3200() {
        assertEquals(
                3200.0,
                Moneda.COP.equivalencia
        );
    }

    @Test
    void USDDebeTenerEquivalenciaUno() {
        assertEquals(
                1.0,
                Moneda.USD.equivalencia
        );
    }

    @Test
    void debeTenerDosMonedas() {
        assertEquals(
                2,
                Moneda.values().length
        );
    }

    @Test
    void debeEncontrarCOPPorNombre() {
        assertEquals(
                Moneda.COP,
                Moneda.valueOf("COP")
        );
    }

    @Test
    void debeEncontrarUSDPorNombre() {
        assertEquals(
                Moneda.USD,
                Moneda.valueOf("USD")
        );
    }

    @Test
    void todasLasMonedasDebenTenerEquivalenciaPositiva() {
        for (Moneda moneda : Moneda.values()) {

            assertTrue(
                    moneda.equivalencia > 0
            );
        }
    }

    @Test
    void todasLasMonedasDebenTenerUnaEquivalenciaFinita() {
        for (Moneda moneda : Moneda.values()) {

            assertTrue(
                    Double.isFinite(moneda.equivalencia)
            );
        }
    }
}