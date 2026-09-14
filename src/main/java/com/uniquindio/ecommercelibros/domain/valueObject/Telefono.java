package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;

public record Telefono(String telefono) {
    public Telefono{
        if (telefono == null) {
            throw new CasillaVaciaException("El telefono no puede estar vacio");
        }
    }
}
