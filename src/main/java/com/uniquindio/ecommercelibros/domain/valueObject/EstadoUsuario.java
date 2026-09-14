package com.uniquindio.ecommercelibros.domain.valueObject;

public enum EstadoUsuario {

    ACTIVO,
    INACTIVO;

    public boolean puedeTransicionarA(EstadoUsuario siguiente) {
        return switch (this) {
            case ACTIVO -> siguiente == INACTIVO;
            case INACTIVO -> siguiente == ACTIVO;
        };
    }

    public boolean estaActivo() {
        return this == ACTIVO;
    }

    public boolean estaInactivo() {
        return this == INACTIVO;
    }
}