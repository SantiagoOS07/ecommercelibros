package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

import java.util.Objects;
import java.util.UUID;

public class ItemCarrito {

    private final UUID idItemCarrito;
    private ISBN isbnLibro;
    private int cantidad;
    private Precio precio;

    public ItemCarrito(UUID idItemCarrito, ISBN isbnLibro, int cantidad, Precio precio) {
        this.idItemCarrito = UUID.randomUUID();
        this.isbnLibro = isbnLibro;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemCarrito that = (ItemCarrito) o;
        return Objects.equals(idItemCarrito, that.idItemCarrito);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idItemCarrito);
    }
}
