package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;

public record Direccion(String direccion) {
    public Direccion{
        if (direccion == null) {
            throw new CasillaVaciaException("El direccion no puede estar vacia");
        }
    }
}
