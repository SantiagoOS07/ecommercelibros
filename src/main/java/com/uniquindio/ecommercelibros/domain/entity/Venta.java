package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

import java.util.UUID;
import java.util.Objects;

public class Venta {

    private final UUID idVenta;
    private final Libro libro;
    private final int cantidad;
    private final Precio precio;

    public Venta(Libro libro, int cantidad, Precio precio) {
        this.idVenta = UUID.randomUUID();
        this.libro = libro;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta venta)) return false;
        return idVenta.equals(venta.idVenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta);
    }
}