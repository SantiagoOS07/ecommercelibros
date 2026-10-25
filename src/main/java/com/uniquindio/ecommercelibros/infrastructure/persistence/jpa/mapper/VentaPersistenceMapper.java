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
        ventaEntity.setPrecioTotal(PrecioEmbeddable.desde(venta.getTotal()));
        ventaEntity.setEstadoVenta(venta.getEstadoPedido());
        ventaEntity.setIdUsuario(venta.getIdUsuario());
        for (ItemVenta itemVenta : venta.getItems()) {
            ItemVentaEntity itemVentaEntity = new ItemVentaEntity();
            itemVentaEntity.setVenta(ventaEntity);
            itemVentaEntity.setIdItemVenta(itemVenta.getIdItemVenta().toString());
            itemVentaEntity.setIsbn(new ISBNEmbeddable(itemVenta.getIsbnLibro().isbn()));
            itemVentaEntity.setCantidad(itemVenta.getCantidad());
            itemVentaEntity.setPrecioUnitario(PrecioEmbeddable.desde(itemVenta.getPrecioUnitario()));
            itemVentaEntity.setPrecioTotal(PrecioEmbeddable.desde(itemVenta.getPrecioTotal()));
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
                        item.getPrecioUnitario().aDominio(),
                        item.getPrecioTotal().aDominio()))
                .toList();

        return Venta.reconstruir(
                UUID.fromString(ventaEntity.getIdVenta()),
                ventaEntity.getIdUsuario(),
                items,
                ventaEntity.getPrecioTotal().aDominio(),
                ventaEntity.getFechaVenta(),
                ventaEntity.getEstadoVenta());
    }
}
