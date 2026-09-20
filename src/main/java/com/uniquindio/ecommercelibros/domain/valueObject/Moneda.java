package com.uniquindio.ecommercelibros.domain.valueObject;

public enum Moneda {
    COP(3200),
    USD(1);

    public final double equivalencia;

    Moneda(double equivalencia){
        this.equivalencia = equivalencia;
    }
}
