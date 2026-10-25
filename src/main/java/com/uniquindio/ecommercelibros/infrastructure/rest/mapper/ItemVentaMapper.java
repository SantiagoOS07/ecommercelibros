package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.venta.ItemVentaResponse;
import com.uniquindio.ecommercelibros.domain.entity.ItemVenta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemVentaMapper {
    public ItemVentaResponse toResponseItem(ItemVenta itemVenta) {
        return new ItemVentaResponse(itemVenta.getIdItemVenta().toString(),
                itemVenta.getIsbnLibro().isbn(),
                itemVenta.getCantidad(),
                itemVenta.getPrecioUnitario().monto(),
                itemVenta.getPrecioTotal().monto(),
                itemVenta.getPrecioTotal().moneda().toString());
    }

    public List<ItemVentaResponse> toResponseItems(List<ItemVenta> items) {
        return items.stream()
                .map(this::toResponseItem)
                .toList();
    }
}
