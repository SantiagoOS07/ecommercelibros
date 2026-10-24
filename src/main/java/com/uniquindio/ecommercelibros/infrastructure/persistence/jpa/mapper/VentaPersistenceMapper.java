package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.ItemVenta;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.ISBNEmbeddable;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.ItemVentaEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.PrecioEmbeddable;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.VentaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class VentaPersistenceMapper {
    public VentaEntity toEntity(Venta venta) {
        VentaEntity ventaEntity = new VentaEntity();
        ventaEntity.setIdVenta(venta.getIdVenta().toString());
        ventaEntity.setFechaVenta(venta.getFecha());
        ventaEntity.setPrecioTotal(new PrecioEmbeddable(venta.getTotal().monto(), venta.getTotal().moneda()));
        ventaEntity.setEstadoVenta(venta.getEstadoPedido());
        ventaEntity.setIdUsuario(venta.getIdUsuario());
        for (ItemVenta itemVenta : venta.getItems()) {
            ItemVentaEntity itemVentaEntity = new ItemVentaEntity();
            itemVentaEntity.setVenta(ventaEntity);
            itemVentaEntity.setIdItemVenta(itemVenta.getIdItemVenta().toString());
            itemVentaEntity.setIsbn(new ISBNEmbeddable(itemVenta.getIsbnLibro().isbn()));
            itemVentaEntity.setCantidad(itemVenta.getCantidad());
            itemVentaEntity.setPrecioUnitario(new PrecioEmbeddable(itemVenta.getPrecioUnitario().monto(), itemVenta.getPrecioUnitario().moneda()));
            itemVentaEntity.setPrecioTotal(new PrecioEmbeddable(itemVenta.getPrecioTotal().monto(), itemVenta.getPrecioTotal().moneda()));
            ventaEntity.getItems().add(itemVentaEntity);
        }
        return ventaEntity;
    }

    public Venta toDomain(VentaEntity ventaEntity) {
        List<ItemVenta> items = ventaEntity.getItems().stream()
                .map(item -> ItemVenta.reconstruir(
                        UUID.fromString(item.getIdItemVenta()),
                        new ISBN(item.getIsbn().getIsbn()),
                        item.getCantidad(),
                        new Precio(item.getPrecioUnitario().getMonto(), item.getPrecioUnitario().getMoneda()),
                        new Precio(item.getPrecioTotal().getMonto(), item.getPrecioTotal().getMoneda())))
                .toList();

        return Venta.reconstruir(
                UUID.fromString(ventaEntity.getIdVenta()),
                ventaEntity.getIdUsuario(),
                items,
                new Precio(ventaEntity.getPrecioTotal().getMonto(), ventaEntity.getPrecioTotal().getMoneda()),
                ventaEntity.getFechaVenta(),
                ventaEntity.getEstadoVenta());
    }
}
