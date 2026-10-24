package com.uniquindio.ecommercelibros.aplication.dto.response.venta;

import com.uniquindio.ecommercelibros.domain.entity.ItemVenta;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;

import java.time.LocalDate;
import java.util.List;

/** GET /ventas/{idVenta}, GET /ventas?idUsuario={idUsuario} */
public record VentaResponse(
        String idVenta,
        List<ItemVenta> items,
        EstadoPedido estadoPedido,
        LocalDate fecha,
        double precio,
        Moneda moneda
) {}
