package com.uniquindio.ecommercelibros.aplication.dto.request.carrito;

import jakarta.validation.constraints.NotBlank;

//POST/Carrito
public record RegistrarCarritoRequest (
        @NotBlank(message = "El id del usuario es obligatorio")
        String idUsuario
)
{}
