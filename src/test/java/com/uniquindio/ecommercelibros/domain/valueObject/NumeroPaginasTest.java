package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumeroPaginasTest {

    @Test
    void debeCrearNumeroDePaginasValido() {
        NumeroPaginas paginas =
                new NumeroPaginas(300);

        assertNotNull(paginas);
        assertEquals(
                300,
                paginas.numeroPaginas()
        );
    }

    @Test
    void debeAceptarUnaPaginaComoValorMinimoValido() {
        NumeroPaginas paginas =
                new NumeroPaginas(1);

        assertEquals(
                1,
                paginas.numeroPaginas()
        );
    }

    @Test
    void debeAceptarUnLibroConMuchasPaginas() {
        NumeroPaginas paginas =
                new NumeroPaginas(5000);

        assertEquals(
                5000,
                paginas.numeroPaginas()
        );
    }

    @Test
    void debeRechazarCeroPaginas() {
        assertThrows(
                CasillaVaciaException.class,
                () -> new NumeroPaginas(0)
        );
    }

    @Test
    void debeRechazarUnaPaginaNegativa() {
        assertThrows(
                CasillaVaciaException.class,
                () -> new NumeroPaginas(-1)
        );
    }

    @Test
    void debeRechazarUnNumeroNegativoGrande() {
        assertThrows(
                CasillaVaciaException.class,
                () -> new NumeroPaginas(-1000)
        );
    }

    @Test
    void debeConservarElNumeroIngresado() {
        NumeroPaginas paginas =
                new NumeroPaginas(250);

        assertEquals(
                250,
                paginas.numeroPaginas()
        );
    }
}