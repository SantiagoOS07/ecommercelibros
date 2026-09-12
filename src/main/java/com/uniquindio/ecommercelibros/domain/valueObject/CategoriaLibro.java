package com.uniquindio.ecommercelibros.domain.valueObject;

public enum CategoriaLibro {

    NOVELA(1.00),
    CIENCIA_FICCION(1.10),
    FANTASIA(1.05),
    MISTERIO(1.08),
    ROMANCE(1.03),
    HISTORIA(1.12),
    FILOSOFIA(1.15),
    CIENCIA(1.18),
    TECNOLOGIA(1.20),
    PROGRAMACION(1.25),
    BIOGRAFIA(1.10),
    POESIA(1.05),
    INFANTIL(0.95),
    JUVENIL(0.98),
    AUTOAYUDA(1.02),
    NEGOCIOS(1.15),
    ARTE(1.20),
    RELIGION(1.05),
    SALUD(1.10),
    EDUCACION(1.15);

    private final double factorPrecio;

    CategoriaLibro(double factorPrecio) {
        this.factorPrecio = factorPrecio;
    }

    public double factorPrecio() {
        return factorPrecio;
    }

    public double calcularPrecio(double precioBase) {
        return precioBase * factorPrecio;
    }
}