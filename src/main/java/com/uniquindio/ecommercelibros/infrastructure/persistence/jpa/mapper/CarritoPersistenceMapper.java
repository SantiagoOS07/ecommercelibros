package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.ItemCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
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
        entity.setEliminado(carrito.isEliminado());

        List<ItemCarritoEntity> itemsEntity = new ArrayList<>();

        for (ItemCarrito item : carrito.getItems()) {
            ItemCarritoEntity itemEntity = new ItemCarritoEntity();
            itemEntity.setIdItemCarrito(item.getIdItemCarrito().toString());
            itemEntity.setCarrito(entity);
            itemEntity.setIsbnLibro(item.getIsbnLibro().isbn());
            itemEntity.setPrecioUnitario(PrecioEmbeddable.desde(item.getPrecioUnitario()));
            itemEntity.setPrecioTotal(PrecioEmbeddable.desde(item.getPrecioTotal()));
            itemEntity.setStockLibro(item.getStockLibro().stock());
            itemEntity.setCantidad(item.getCantidad());
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
                        item.getPrecioUnitario().aDominio(),
                        new Stock(item.getStockLibro()),
                        item.getCantidad(),
                        item.getPrecioTotal().aDominio()
                ))
                .toList();

        return Carrito.reconstruir(
                UUID.fromString(entity.getIdCarrito()),
                entity.getIdUsuario(),
                entity.getEstado(),
                items,
                entity.isEliminado());
    }
}