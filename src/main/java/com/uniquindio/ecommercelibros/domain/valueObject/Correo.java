package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

public record Correo(String correo) {
    public Correo{
        if (correo == null) {
            throw new CasillaVaciaException("El correo no puede ser vacio");
        }
        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]++\\.[A-Za-z]{2,}$")) {
            throw new ReglaDominioException("El correo no es válido");
        }
    }
}
