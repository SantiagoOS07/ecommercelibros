package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

import java.time.LocalDate;

public record Descuento(double porcentaje, String motivo, LocalDate fechaInicio, LocalDate fechaFin) {

    public Descuento{
        if (porcentaje < 0) {
            throw new ReglaDominioException("El precio no puede ser negativo");
        }
        if (porcentaje > 100) {
            throw new ReglaDominioException("El porcentaje no puede ser mayor a 100");
        }
        if (motivo == null) {
            throw new CasillaVaciaException("El motivo es obligatorio");
        }
        if (motivo.length() > 100) {
            throw new ReglaDominioException("El motivo no puede tener mas de 100 caracteres");
        }
        if (fechaInicio == null) {
            throw new CasillaVaciaException("La fecha de inicio es obligatoria");
        }
        if (fechaFin == null) {
            throw new CasillaVaciaException("La fecha de fin es obligatoria");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ReglaDominioException("La fecha de fin no puede ser antes de la fecha de inicio");
        }
    }

    public boolean esVigente() {
        return LocalDate.now().isAfter(fechaInicio) && LocalDate.now().isBefore(fechaFin);
    }
}
