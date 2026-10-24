package com.uniquindio.ecommercelibros.aplication.dto.request.venta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/** PUT	/ventas/{idVenta}/reembolso */
public record SolicitarReembolsoVentaRequest(
        @NotNull(message = "El id de la venta no puede ser nulo")
        UUID idVenta,

        @Size(message = "El motivo del reembolso no puede estar vacío")
        String motivo
) {}
