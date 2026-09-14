package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.StockInsuficienteException;

public record Stock(int stock) {
    public Stock{
        if (stock < 0) {
            throw new StockInsuficienteException("El stock no puede ser negativo");
        }
    }
}
