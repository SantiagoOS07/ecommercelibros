package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

import java.util.Objects;
import java.util.UUID;

public class ItemVenta {
    private final UUID idItemVenta;
    private ISBN isbnLibro;
    private int cantidad;
    private Precio precioUnitario;
    private Precio subTotal;

    public ItemVenta(UUID idItemVenta, ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio subTotal) {
        this.idItemVenta = UUID.randomUUID();
        this.isbnLibro = isbnLibro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemVenta itemVenta = (ItemVenta) o;
        return Objects.equals(idItemVenta, itemVenta.idItemVenta);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idItemVenta);
    }
}
