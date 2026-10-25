package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.venta.VentaResponse;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VentaMapper {
    private final ItemVentaMapper itemVentaMapper;

    public VentaResponse toResponseVenta(Venta venta) {
        return new VentaResponse(venta.getIdVenta().toString(),
                itemVentaMapper.toResponseItems(venta.getItems()),
                venta.getEstadoPedido(),
                venta.getFecha(),
                venta.getTotal().monto(),
                venta.getTotal().moneda());
    }

    public List<VentaResponse> toResponseVentas(List<Venta> ventas) {
        return ventas.stream().map(this::toResponseVenta).toList();
    }
}
