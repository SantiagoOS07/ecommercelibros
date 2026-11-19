package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

import java.time.LocalDate;

public record Descuento(double porcentaje, String motivo, LocalDate fechaInicio, LocalDate fechaFin) {

    public Descuento {
        if (porcentaje <= 0 || porcentaje >= 100) {
            throw new ReglaDominioException("El porcentaje del descuento debe ser mayor que 0 y menor que 100");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new CasillaVaciaException("El motivo del descuento es obligatorio");
        }
        if (motivo.length() > 100) {
            throw new ReglaDominioException("El motivo del descuento no puede tener más de 100 caracteres");
        }
        if (fechaInicio == null) {
            throw new CasillaVaciaException("La fecha de inicio es obligatoria");
        }
        if (fechaFin == null) {
            throw new CasillaVaciaException("La fecha de fin es obligatoria");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ReglaDominioException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    public boolean estaVigente(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }
}

