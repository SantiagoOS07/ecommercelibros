package com.uniquindio.ecommercelibros.domain.valueObject;

public enum EstadoCarrito {
    CREADO,
    PROCESANDO,
    VENDIDO;

    public boolean puedeTransicionarA(EstadoCarrito siguiente) {
        return switch (this) {
            case CREADO -> siguiente == PROCESANDO;
            case PROCESANDO -> siguiente == VENDIDO;
            case VENDIDO -> false;
        };
    }

    public boolean esFinal() {
        return this == VENDIDO;
    }
}