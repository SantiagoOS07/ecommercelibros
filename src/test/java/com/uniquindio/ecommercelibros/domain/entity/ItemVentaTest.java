package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemVentaTest {

    private static final String ISBN_VALIDO = "9783161484100";

    @Test
    void crear_conDatosValidos_conservaLosDatosYGeneraId() {
        ISBN isbn = new ISBN(ISBN_VALIDO);
        Precio precioUnitario = new Precio(25000, Moneda.COP);
        Precio precioTotal = new Precio(50000, Moneda.COP);

        ItemVenta item = ItemVenta.crear(isbn, 2, precioUnitario, precioTotal);

        assertNotNull(item.getIdItemVenta());
        assertEquals(isbn, item.getIsbnLibro());
        assertEquals(2, item.getCantidad());
        assertEquals(precioUnitario, item.getPrecioUnitario());
        assertEquals(precioTotal, item.getPrecioTotal());
    }

    @Test
    void crear_dosVecesConLosMismosDatos_generaEntidadesDiferentes() {
        ItemVenta item1 = crearItem();
        ItemVenta item2 = crearItem();

        assertNotEquals(item1, item2);
        assertNotEquals(item1.getIdItemVenta(), item2.getIdItemVenta());
    }

    @Test
    void crear_conIsbnNulo_lanzaCasillaVacia() {
        Precio precioUnitario = new Precio(25000, Moneda.COP);
        Precio precioTotal = new Precio(50000, Moneda.COP);

        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class,
                () -> ItemVenta.crear(null, 2, precioUnitario, precioTotal));

        assertEquals("El ISBN del libro no puede ser nulo.", excepcion.getMessage());
    }


    @Test
    void crear_conPrecioUnitarioNulo_lanzaCasillaVacia() {
        ISBN isbn = new ISBN(ISBN_VALIDO);
        Precio precioTotal = new Precio(50000, Moneda.COP);

        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class,
                () -> ItemVenta.crear(isbn, 2, null, precioTotal));

        assertEquals("El precio unitario no puede ser nulo.", excepcion.getMessage());
    }

    @Test
    void crear_conPrecioTotalNulo_lanzaCasillaVacia() {
        ISBN isbn = new ISBN(ISBN_VALIDO);
        Precio precioUnitario = new Precio(25000, Moneda.COP);

        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class,
                () -> ItemVenta.crear(isbn, 2, precioUnitario, null));

        assertEquals("El precio total no puede ser nulo.", excepcion.getMessage());
    }


    @Test
    void reconstruir_conDatosPersistidos_conservaElIdYLosDatos() {
        UUID id = UUID.randomUUID();
        ISBN isbn = new ISBN(ISBN_VALIDO);
        Precio precioUnitario = new Precio(25000, Moneda.COP);
        Precio precioTotal = new Precio(75000, Moneda.COP);

        ItemVenta item = ItemVenta.reconstruir(id, isbn, 3, precioUnitario, precioTotal);

        assertEquals(id, item.getIdItemVenta());
        assertEquals(isbn, item.getIsbnLibro());
        assertEquals(3, item.getCantidad());
        assertEquals(precioUnitario, item.getPrecioUnitario());
        assertEquals(precioTotal, item.getPrecioTotal());
    }

    @Test
    void equals_dosItemsConElMismoId_sonIgualesAunqueSusDatosDifieran() {
        UUID id = UUID.randomUUID();
        ItemVenta item1 = ItemVenta.reconstruir(id, new ISBN(ISBN_VALIDO), 1,
                new Precio(25000, Moneda.COP), new Precio(25000, Moneda.COP));
        ItemVenta item2 = ItemVenta.reconstruir(id, new ISBN("9789587580188"), 5,
                new Precio(10000, Moneda.COP), new Precio(50000, Moneda.COP));

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    private static ItemVenta crearItem() {
        return ItemVenta.crear(
                new ISBN(ISBN_VALIDO),
                2,
                new Precio(25000, Moneda.COP),
                new Precio(50000, Moneda.COP));
    }
}