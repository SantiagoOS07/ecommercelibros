package com.uniquindio.ecommercelibros.domain.valueObject;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

public record Precio(double monto, String moneda) {

    public Precio {
        if (monto < 0) {
            throw new ReglaDominioException("El precio no puede ser negativo");
        }
        if (moneda == null || moneda.isBlank()) {
            throw new ReglaDominioException("La moneda es obligatoria");
        }
    }

    public Precio aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new ReglaDominioException("El descuento debe estar entre 0 y 100");
        }
        double nuevoMonto = monto - (monto * porcentaje / 100);
        return new Precio(nuevoMonto, moneda);
    }
}