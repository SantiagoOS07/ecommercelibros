package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

public record ISBN(String isbn) {
    public ISBN {
        if (isbn.length() != 13 ) {
            throw new ReglaDominioException("El ISBN debe tener 13 digitos");
        }
        if (isbn.isBlank()) {
            throw new CasillaVaciaException("El ISBN es obligatorio");
        }
    }
}
