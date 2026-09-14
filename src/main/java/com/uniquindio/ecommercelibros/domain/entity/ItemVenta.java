package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

public class ItemVenta {
    private Libro libro;
    private int cantidad;
    private Precio precioUnitario;
    private Precio subTotal;

    public ItemVenta(Libro libro, int cantidad, Precio precioUnitario, Precio subTotal) {
        this.libro = libro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
    }
}
