package com.uniquindio.ecommercelibros.domain.exception;

public class LibroSinStockException extends RuntimeException {
    public LibroSinStockException(String message) {
        super(message);
    }
}
