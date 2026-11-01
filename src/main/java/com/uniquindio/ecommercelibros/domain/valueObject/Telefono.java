package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;

public record Telefono(String telefono) {
    public Telefono {
        if (telefono == null || telefono.isBlank()) {
            throw new CasillaVaciaException("El teléfono no puede estar vacío");
        }
    }
}