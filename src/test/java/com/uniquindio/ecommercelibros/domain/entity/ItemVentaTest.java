package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemVentaTest {

    @Test
    void dosItemsVentaConDatosDiferentesDebenSerEntidadesDiferentes() {
        ItemVenta item1 = crearItemVenta();
        ItemVenta item2 = crearItemVenta();

        boolean sonDiferentes = !item1.equals(item2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearItemVentaSinISBNDelLibro() {
        assertThrows(CasillaVaciaException.class,
                () -> ItemVenta.crear(
                        null,
                        2,
                        new Precio(25000, Moneda.COP),
                        new Precio(50000, Moneda.COP)
                ));
    }

    @Test
    void noDebePermitirCambiarCantidadPorCero() {
        ItemVenta item = crearItemVenta();

        int cantidadOriginal = item.getCantidad();
        Precio subtotalOriginal = item.getSubTotal();

        assertThrows(CasillaVaciaException.class,
                () -> item.cambiarCantidad(
                        0,
                        new Precio(75000, Moneda.COP)
                ));

        assertEquals(cantidadOriginal, item.getCantidad());
        assertEquals(subtotalOriginal, item.getSubTotal());
    }

    @Test
    void noDebePermitirCambiarPrecioUnitarioPorUnoNulo() {
        ItemVenta item = crearItemVenta();

        Precio precioOriginal = item.getPrecioUnitario();

        assertThrows(CasillaVaciaException.class,
                () -> item.cambiarPrecioUnitario(null));

        assertEquals(precioOriginal, item.getPrecioUnitario());
    }

    private ItemVenta crearItemVenta() {
        return ItemVenta.crear(
                new ISBN("9783161484100"),
                2,
                new Precio(25000, Moneda.COP),
                new Precio(50000, Moneda.COP)
        );
    }

}
