package com.uniquindio.ecommercelibros.infraestructure.persistence;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.ItemCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.CarritoPersistenceMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarritoPersistenceMapperTest {

    private final CarritoPersistenceMapper mapper = new CarritoPersistenceMapper();

    @Test
    void toEntityDebeAsignarPrecioTotalYStockComoEntero() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN("9783161484100"), 2, new Precio(10, Moneda.USD), new Stock(7));

        CarritoEntity entity = mapper.toEntity(carrito);

        assertNotNull(entity.getPrecioTotal());
        assertEquals(20.0, entity.getPrecioTotal().doubleValue(), 0.0001);
        assertEquals(7, entity.getItems().getFirst().getStockLibro());
    }

    @Test
    void idDelItemYTotalDebenSobrevivirAlViajeEntidadDominio() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN("9783161484100"), 3, new Precio(10.5, Moneda.USD), new Stock(7));
        ItemCarrito original = carrito.getItems().getFirst();

        Carrito reconstruido = mapper.toDomain(mapper.toEntity(carrito));
        ItemCarrito item = reconstruido.getItems().getFirst();

        assertEquals(carrito.getIdCarrito(), reconstruido.getIdCarrito());
        assertEquals(original.getIdItemCarrito(), item.getIdItemCarrito());
        assertEquals(original.getPrecioTotal(), item.getPrecioTotal());
        assertEquals(original.getStockLibro(), item.getStockLibro());
        assertEquals(carrito.getPrecioTotal(), reconstruido.getPrecioTotal());
    }
}
