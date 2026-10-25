package com.uniquindio.ecommercelibros.aplication.dto.request.carrito;

import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

//PUT/Carrito
public record ActualizarCantidadItemCarritoRequest (

        @NotBlank(message = "El id del carrito no puede ser vacio")
        UUID idCarrito,

        @NotBlank(message = "El isbn del libro es obligatorio")
        ISBN isbn,

        @NotBlank(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        int cantidadLibro,

        @NotBlank(message = "El stock no puede ser vacio")
        Stock stockLibro
)
{}
