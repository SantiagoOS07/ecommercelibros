package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.ItemCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.ItemCarritoEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.PrecioEmbeddable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CarritoPersistenceMapper {

    public CarritoEntity toEntity(Carrito carrito) {

        CarritoEntity entity = new CarritoEntity();

        entity.setIdCarrito(carrito.getIdCarrito().toString());
        entity.setIdUsuario(carrito.getIdUsuario());
        entity.setEstado(carrito.getEstado());

        List<ItemCarritoEntity> itemsEntity = new ArrayList<>();

        for (ItemCarrito item : carrito.getItems()) {

            ItemCarritoEntity itemEntity = new ItemCarritoEntity();

            itemEntity.setIdItemCarrito(
                    item.getIdItemCarrito().toString()
            );

            itemEntity.setCarrito(entity);
            itemEntity.setIsbnLibro(item.getIsbnLibro().isbn());

            itemEntity.setPrecioUnitario(
                    new PrecioEmbeddable(
                            item.getPrecioUnitario().monto(),
                            item.getPrecioUnitario().moneda()
                    )
            );

            itemEntity.setStockLibro(item.getStockLibro());
            itemEntity.setCantidad(item.getCantidad());

            itemEntity.setPrecioTotal(
                    new PrecioEmbeddable(
                            item.getPrecioTotal().monto(),
                            item.getPrecioTotal().moneda()
                    )
            );

            itemsEntity.add(itemEntity);
        }

        entity.setItems(itemsEntity);

        return entity;
    }

    public Carrito toDomain(CarritoEntity entity) {

        List<ItemCarrito> items = entity.getItems()
                .stream()
                .map(item -> ItemCarrito.reconstruir(
                        UUID.fromString(item.getIdItemCarrito()),
                        new ISBN(item.getIsbnLibro()),
                        new Precio(
                                item.getPrecioUnitario().getMonto(),
                                item.getPrecioUnitario().getMoneda()
                        ),
                        new Stock(item.getStockLibro().stock()),
                        item.getCantidad(),
                        new Precio(
                                item.getPrecioTotal().getMonto(),
                                item.getPrecioTotal().getMoneda()
                        )
                ))
                .toList();

        return Carrito.reconstruir(
                UUID.fromString(entity.getIdCarrito()),
                entity.getIdUsuario(),
                entity.getEstado(),
                items
        );
    }
}
