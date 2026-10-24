package com.uniquindio.ecommercelibros.aplication.dto.request.venta;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** PUT	/ventas/{idVenta}/estado, PUT /ventas/{idVenta}/cancelar */
public record ActualizarEstadoVentaRequest(
        @NotNull(message = "El id de la venta no puede ser nulo")
        UUID idVenta
) {}
