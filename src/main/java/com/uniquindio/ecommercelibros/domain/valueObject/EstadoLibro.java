package com.uniquindio.ecommercelibros.domain.valueObject;

public enum EstadoLibro {

    DISPONIBLE,
    AGOTADO,
    DESCONTINUADO;

    public boolean puedeTransicionarA(EstadoLibro siguiente) {
        return switch (this) {
            case DISPONIBLE ->
                    siguiente == AGOTADO ||
                            siguiente == DESCONTINUADO;

            case AGOTADO ->
                    siguiente == DISPONIBLE ||
                            siguiente == DESCONTINUADO;

            case DESCONTINUADO ->
                    false;
        };
    }

    public boolean esFinal() {
        return this == DESCONTINUADO;
    }

    public boolean estaDisponible() {
        return this == DISPONIBLE;
    }

    public boolean estaAgotado() {
        return this == AGOTADO;
    }

    public boolean estaDescontinuado() {
        return this == DESCONTINUADO;
    }
}