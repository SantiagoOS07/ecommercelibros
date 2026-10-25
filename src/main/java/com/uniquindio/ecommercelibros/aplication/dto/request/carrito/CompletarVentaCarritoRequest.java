package com.uniquindio.ecommercelibros.aplication.dto.request.carrito;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CompletarVentaCarritoRequest (

        @NotBlank(message = "El id del carrito no puede ser vacio")
        UUID idCarrito
)
{}
