package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    @Test
    void dosVentasConDatosDiferentesDebenSerEntidadesDiferentes() {
        Venta venta1 = crearVenta();
        Venta venta2 = crearVenta();

        boolean sonDiferentes = !venta1.equals(venta2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearVentaSinItems() {
        List<ItemVenta> items = new ArrayList<>();
        Precio total = new Precio(50000, Moneda.COP);

        assertThrows(CasillaVaciaException.class,
                () -> Venta.crear(items, total));
    }

    @Test
    void noDebePermitirAgregarItemNulo() {
        Venta venta = crearVenta();

        int cantidadOriginal = venta.getItems().size();

        assertThrows(CasillaVaciaException.class,
                () -> venta.agregarItem(null));

        assertEquals(cantidadOriginal, venta.getItems().size());
    }

    @Test
    void noDebePermitirEliminarElUltimoItemDeLaVenta() {
        Venta venta = crearVenta();
        ItemVenta item = venta.getItems().get(0);

        int cantidadOriginal = venta.getItems().size();

        assertThrows(CasillaVaciaException.class,
                () -> venta.eliminarItem(item));

        assertEquals(cantidadOriginal, venta.getItems().size());
        assertEquals(EstadoPedido.EN_PROCESO, venta.getEstadoPedido());
    }

    private Venta crearVenta() {
        ItemVenta item = ItemVenta.crear(
                new ISBN("9783161484100"),
                2,
                new Precio(25000, Moneda.COP),
                new Precio(50000, Moneda.COP)
        );

        List<ItemVenta> items = new ArrayList<>();
        items.add(item);

        return Venta.crear(
                items,
                new Precio(50000, Moneda.COP)
        );
    }
}
