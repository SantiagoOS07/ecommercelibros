package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;

public record Direccion(String direccion) {
    public Direccion {
        if (direccion == null || direccion.isBlank()) {
            throw new CasillaVaciaException("La dirección no puede estar vacía");
        }
    }
}