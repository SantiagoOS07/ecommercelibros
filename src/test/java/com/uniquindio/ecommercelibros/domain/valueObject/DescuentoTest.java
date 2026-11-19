package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DescuentoTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 11, 1);
    private static final LocalDate FIN = LocalDate.of(2026, 12, 31);

    @Test
    void debeCrearDescuentoValido() {
        Descuento descuento = new Descuento(20, "Temporada navideña", INICIO, FIN);

        assertEquals(20, descuento.porcentaje());
        assertEquals("Temporada navideña", descuento.motivo());
        assertEquals(INICIO, descuento.fechaInicio());
        assertEquals(FIN, descuento.fechaFin());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10, 0, 100, 150})
    void noDebePermitirPorcentajesFueraDeRango(double porcentaje) {
        assertThrows(ReglaDominioException.class,
                () -> new Descuento(porcentaje, "Motivo", INICIO, FIN));
    }

    @Test
    void debePermitirPorcentajesCercanosALosLimites() {
        assertDoesNotThrow(() -> new Descuento(0.01, "Motivo", INICIO, FIN));
        assertDoesNotThrow(() -> new Descuento(99.99, "Motivo", INICIO, FIN));
    }

    @Test
    void noDebePermitirMotivoNulo() {
        assertThrows(CasillaVaciaException.class,
                () -> new Descuento(20, null, INICIO, FIN));
    }

    @Test
    void noDebePermitirMotivoVacio() {
        assertThrows(CasillaVaciaException.class,
                () -> new Descuento(20, "   ", INICIO, FIN));
    }

    @Test
    void noDebePermitirMotivoDeMasDeCienCaracteres() {
        assertThrows(ReglaDominioException.class,
                () -> new Descuento(20, "a".repeat(101), INICIO, FIN));
    }

    @Test
    void debePermitirMotivoDeExactamenteCienCaracteres() {
        assertDoesNotThrow(() -> new Descuento(20, "a".repeat(100), INICIO, FIN));
    }

    @Test
    void noDebePermitirFechaDeInicioNula() {
        assertThrows(CasillaVaciaException.class,
                () -> new Descuento(20, "Motivo", null, FIN));
    }

    @Test
    void noDebePermitirFechaDeFinNula() {
        assertThrows(CasillaVaciaException.class,
                () -> new Descuento(20, "Motivo", INICIO, null));
    }

    @Test
    void noDebePermitirFechaDeFinAnteriorALaDeInicio() {
        assertThrows(ReglaDominioException.class,
                () -> new Descuento(20, "Motivo", FIN, INICIO));
    }

    @Test
    void debePermitirQueInicioYFinSeanElMismoDia() {
        assertDoesNotThrow(() -> new Descuento(20, "Motivo", INICIO, INICIO));
    }

    @Test
    void estaVigenteEntreInicioYFin() {
        Descuento descuento = new Descuento(20, "Motivo", INICIO, FIN);

        assertTrue(descuento.estaVigente(LocalDate.of(2026, 11, 19)));
    }

    @Test
    void estaVigenteElDiaDeInicioYElDiaDeFin() {
        Descuento descuento = new Descuento(20, "Motivo", INICIO, FIN);

        assertTrue(descuento.estaVigente(INICIO));
        assertTrue(descuento.estaVigente(FIN));
    }

    @Test
    void noEstaVigenteAntesDelInicio() {
        Descuento descuento = new Descuento(20, "Motivo", INICIO, FIN);

        assertFalse(descuento.estaVigente(INICIO.minusDays(1)));
    }

    @Test
    void noEstaVigenteDespuesDelFin() {
        Descuento descuento = new Descuento(20, "Motivo", INICIO, FIN);

        assertFalse(descuento.estaVigente(FIN.plusDays(1)));
    }
}