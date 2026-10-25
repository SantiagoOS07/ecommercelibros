package com.uniquindio.ecommercelibros.infraestructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.CarritoResponse;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.ItemCarritoMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarritoMapperTest {

    private final CarritoMapper mapper = new CarritoMapper(new ItemCarritoMapper());

    @Test
    void debeResolverElTituloDelLibroPorIsbn() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN("9783161484100"), 1, new Precio(10, Moneda.USD), new Stock(5));

        CarritoResponse response = mapper.toResponse(carrito, Map.of("9783161484100", "Cien años de soledad"));

        assertEquals("Cien años de soledad", response.items().getFirst().tituloLibro());
    }

    @Test
    void siNoHayTituloDebeUsarElTextoPorDefecto() {
        Carrito carrito = Carrito.crear("user-1");
        carrito.agregarItem(new ISBN("9783161484100"), 1, new Precio(10, Moneda.USD), new Stock(5));

        CarritoResponse response = mapper.toResponse(carrito, Map.of());

        assertEquals("Libro no disponible", response.items().getFirst().tituloLibro());
    }
}
