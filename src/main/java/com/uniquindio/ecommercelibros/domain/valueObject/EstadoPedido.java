package com.uniquindio.ecommercelibros.domain.valueObject;

public enum EstadoPedido {

    EN_PROCESO,
    ESPERANDO_PAGO,
    ENVIADO,
    ENTREGADO,
    CANCELADO;

    public boolean puedeTransicionarA(EstadoPedido siguiente) {
        return switch (this) {

            case EN_PROCESO ->
                    siguiente == ESPERANDO_PAGO ||
                            siguiente == CANCELADO;

            case ESPERANDO_PAGO ->
                    siguiente == ENVIADO ||
                            siguiente == CANCELADO;

            case ENVIADO ->
                    siguiente == ENTREGADO;

            case ENTREGADO,
                 CANCELADO ->
                    false;
        };
    }

    public boolean esFinal() {
        return this == ENTREGADO ||
                this == CANCELADO;
    }

    public boolean estaEnProceso() {
        return this == EN_PROCESO;
    }

    public boolean estaEsperandoPago() {
        return this == ESPERANDO_PAGO;
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
}