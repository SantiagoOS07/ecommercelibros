package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

public class ItemCarrito {

    private Libro libro;
    private int cantidad;
    private Precio precio;

    public ItemCarrito(Libro libro, int cantidad, Precio precio) {
        this.libro = libro;
        this.cantidad = cantidad;
        this.precio = precio;
    }

}
