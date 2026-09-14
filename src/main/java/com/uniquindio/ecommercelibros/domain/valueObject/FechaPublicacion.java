package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

import java.time.LocalDate;

public record FechaPublicacion(LocalDate fecha) {
    public FechaPublicacion {
        if (fecha == null) {
            throw new CasillaVaciaException("La fecha de publicación no puede ser nula");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new ReglaDominioException("La fecha de publicación no puede ser posterior a la fecha actual");
        }
    }

    public Integer dia() {
        return fecha.getDayOfMonth();
    }

    public Integer mes() {
        return fecha.getMonthValue();
    }

    public Integer anio() {
        return fecha.getYear();
    }
}
