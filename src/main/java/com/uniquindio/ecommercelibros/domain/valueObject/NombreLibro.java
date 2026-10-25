package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

public record NombreLibro(String nombreLibro) {
    public NombreLibro {
        if  (nombreLibro == null || nombreLibro.isBlank()) {
            throw new CasillaVaciaException("El nombre del libro no puede ser vacio");
        }
    }
}
