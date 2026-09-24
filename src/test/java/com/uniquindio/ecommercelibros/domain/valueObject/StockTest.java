package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void debeCrearStockPositivo() {
        Stock stock = new Stock(20);

        assertNotNull(stock);
        assertEquals(
                20,
                stock.stock()
        );
    }

    @Test
    void debePermitirStockCero() {
        Stock stock = new Stock(0);

        assertEquals(
                0,
                stock.stock()
        );
    }

    @Test
    void debeAceptarStockDeUnElemento() {
        Stock stock = new Stock(1);

        assertEquals(
                1,
                stock.stock()
        );
    }

    @Test
    void debeAceptarStockGrande() {
        Stock stock = new Stock(10000);

        assertEquals(
                10000,
                stock.stock()
        );
    }

    @Test
    void debeRechazarStockNegativo() {
        assertThrows(
                StockInsuficienteException.class,
                () -> new Stock(-1)
        );
    }

    @Test
    void debeRechazarStockNegativoGrande() {
        assertThrows(
                StockInsuficienteException.class,
                () -> new Stock(-1000)
        );
    }

    @Test
    void debeConservarElValorIngresado() {
        Stock stock = new Stock(35);

        assertEquals(
                35,
                stock.stock()
        );
    }
}