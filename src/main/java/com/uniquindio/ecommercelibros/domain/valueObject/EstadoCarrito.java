package com.uniquindio.ecommercelibros.domain.valueObject;

public enum EstadoCarrito {

    PROCESANDO,
    VENDIDO;

    public boolean puedeTransicionarA(EstadoCarrito siguiente) {
        return switch (this) {
            case PROCESANDO -> siguiente == VENDIDO;
            case VENDIDO -> false;
        };
    }

    public boolean esFinal() {
        return this == VENDIDO;
    }
}