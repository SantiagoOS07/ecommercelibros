package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrecioTest {

    @Test
    void debeCrearPrecioValidoEnCOP() {
        Precio precio =
                new Precio(50000.0, Moneda.COP);

        assertNotNull(precio);
        assertEquals(
                50000.0,
                precio.monto()
        );
        assertEquals(
                Moneda.COP,
                precio.moneda()
        );
    }

    @Test
    void debeCrearPrecioValidoEnUSD() {
        Precio precio =
                new Precio(100.0, Moneda.USD);

        assertEquals(
                100.0,
                precio.monto()
        );
        assertEquals(
                Moneda.USD,
                precio.moneda()
        );
    }

    @Test
    void debePermitirPrecioCero() {
        Precio precio =
                new Precio(0.0, Moneda.COP);

        assertEquals(
                0.0,
                precio.monto()
        );
    }

    @Test
    void debeRechazarPrecioNegativo() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Precio(-1.0, Moneda.COP)
        );
    }

    @Test
    void debeRechazarPrecioNegativoGrande() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Precio(-100000.0, Moneda.COP)
        );
    }

    @Test
    void debeRechazarMonedaNull() {
        CasillaVaciaException excepcion =
                assertThrows(
                        CasillaVaciaException.class,
                        () -> new Precio(50000.0, null)
                );

        assertEquals(
                "La moneda es obligatoria",
                excepcion.getMessage()
        );
    }

    @Test
    void debeAplicarDescuentoDel10PorCiento() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        Precio resultado =
                precio.aplicarDescuento(10);

        assertEquals(
                90000.0,
                resultado.monto()
        );
    }

    @Test
    void debeAplicarDescuentoDel25PorCiento() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        Precio resultado =
                precio.aplicarDescuento(25);

        assertEquals(
                75000.0,
                resultado.monto()
        );
    }

    @Test
    void debeAplicarDescuentoDel50PorCiento() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        Precio resultado =
                precio.aplicarDescuento(50);

        assertEquals(
                50000.0,
                resultado.monto()
        );
    }

    @Test
    void debePermitirDescuentoDelCeroPorCiento() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        Precio resultado =
                precio.aplicarDescuento(0);

        assertEquals(
                100000.0,
                resultado.monto()
        );
    }

    @Test
    void debePermitirDescuentoDelCienPorCiento() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        Precio resultado =
                precio.aplicarDescuento(100);

        assertEquals(
                0.0,
                resultado.monto()
        );
    }

    @Test
    void debeMantenerLaMonedaDespuesDelDescuento() {
        Precio precio =
                new Precio(100000.0, Moneda.USD);

        Precio resultado =
                precio.aplicarDescuento(20);

        assertEquals(
                Moneda.USD,
                resultado.moneda()
        );
    }

    @Test
    void debeRechazarDescuentoNegativo() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        assertThrows(
                ReglaDominioException.class,
                () -> precio.aplicarDescuento(-1)
        );
    }

    @Test
    void debeRechazarDescuentoMayorAlCien() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        assertThrows(
                ReglaDominioException.class,
                () -> precio.aplicarDescuento(101)
        );
    }

    @Test
    void debeRechazarDescuentoMuchoMayorAlCien() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        assertThrows(
                ReglaDominioException.class,
                () -> precio.aplicarDescuento(200)
        );
    }

    @Test
    void elDescuentoNoDebeModificarElPrecioOriginal() {
        Precio precio =
                new Precio(100000.0, Moneda.COP);

        precio.aplicarDescuento(20);

        assertEquals(
                100000.0,
                precio.monto()
        );
    }
}