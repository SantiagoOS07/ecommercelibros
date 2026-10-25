package com.uniquindio.ecommercelibros.aplication.dto.request.carrito;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

//PUT/Carrito
public record CancelarConfirmacionCarritoRequest (

        @NotBlank(message = "El id del carrito no puede ser vacio")
        UUID idCarrito
)

{}
