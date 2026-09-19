package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

import java.util.Objects;
import java.util.UUID;

public class ItemCarrito {

    private final UUID idItemCarrito;
    private final ISBN isbnLibro;
    private int cantidad;
    private Precio precioTotal;

    private ItemCarrito(ISBN isbnLibro, int cantidad, Precio precioLibro) {
        this.idItemCarrito = UUID.randomUUID();
        this.isbnLibro = isbnLibro;
        this.cantidad = cantidad;
        this.precioTotal = new Precio(precioLibro.monto() * cantidad, precioLibro.moneda());
    }

    public static ItemCarrito crear(ISBN isbnLibro, int cantidad, Precio precioLibro) {
        verificar(isbnLibro, cantidad, precioLibro);
        return new ItemCarrito(isbnLibro, cantidad, precioLibro);
    }

    private static void verificar(ISBN isbnLibro, int cantidad, Precio precioLibro) {

    }

    public void actualizarCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.precioTotal = new Precio(precioTotal.monto() * cantidad, precioTotal.moneda());
    }

    public void actualizarPrecio(Precio nuevoPrecioLibro) {
        this.precioTotal = new Precio(nuevoPrecioLibro.monto() * cantidad, nuevoPrecioLibro.moneda());
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
