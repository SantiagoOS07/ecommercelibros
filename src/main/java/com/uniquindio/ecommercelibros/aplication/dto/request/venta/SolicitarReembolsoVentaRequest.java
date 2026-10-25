package com.uniquindio.ecommercelibros.aplication.dto.request.venta;

import jakarta.validation.constraints.NotBlank;

/** PUT	/ventas/{idVenta}/reembolso */
public record SolicitarReembolsoVentaRequest(
        @NotBlank(message = "El motivo del reembolso es obligatorio")
        String motivo
) {}
