package com.uniquindio.ecommercelibros.domain.valueObject;

public enum EstadoPedido {

    PREPARANDO,
    ENVIADO,
    ENTREGADO,
    CANCELADO,
    REEMBOLSADO;

    public boolean puedeTransicionarA(EstadoPedido siguiente) {
        return switch (this) {
            case PREPARANDO ->
                    siguiente == ENVIADO ||
                            siguiente == CANCELADO;

            case ENVIADO ->
                    siguiente == ENTREGADO;

            case CANCELADO, ENTREGADO ->
                    siguiente == REEMBOLSADO;

            case REEMBOLSADO ->
                    false;
        };
    }

    public boolean esFinal() {
        return this == ENTREGADO ||
                this == REEMBOLSADO;
    }

    public boolean estaEnProceso() {
        return this == PREPARANDO;
    }

    public boolean fueEnviado() {
        return this == ENVIADO;
    }

    public boolean fueEntregado() {
        return this == ENTREGADO;
    }

    public boolean fueCancelado() {
        return this == CANCELADO;
    }

    public boolean fueReembolsado() {
        return this == REEMBOLSADO;
    }
}