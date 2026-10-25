package com.uniquindio.ecommercelibros.aplication.dto.response.venta;

public record ItemVentaResponse(
        String idItemVenta,
        String isbnLibro,
        int cantidad,
        double precioUnitario,
        double precioTotal,
        String moneda
) {}
