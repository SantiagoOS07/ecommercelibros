
package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.CarritoResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.ItemCarritoResponse;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CarritoMapper {

    private final ItemCarritoMapper itemCarritoMapper;

    public CarritoMapper(ItemCarritoMapper itemCarritoMapper) {
        this.itemCarritoMapper = itemCarritoMapper;
    }

    public CarritoResponse toResponse(
            Carrito carrito,
            Map<String, String> titulosLibros) {

        List<ItemCarritoResponse> items = carrito.getItems()
                .stream()
                .map(item -> itemCarritoMapper.toResponse(
                        item,
                        titulosLibros.getOrDefault(
                                item.getIsbnLibro().toString(),
                                "Libro no disponible"
                        )
                ))
                .collect(Collectors.toList());

        return new CarritoResponse(
                carrito.getIdCarrito(),
                items,
                carrito.getPrecioTotal().monto()
        );
    }


    public Carrito toDomain(String idUsuario) {
        return Carrito.crear(idUsuario);
    }

}
