package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class ItemVenta {

    private final UUID idItemVenta;
    private ISBN isbnLibro;
    private int cantidad;
    private Precio precioUnitario;
    private Precio subTotal;

    private ItemVenta(UUID idItemVenta, ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio subTotal) {
        this.idItemVenta = idItemVenta;
        this.isbnLibro = isbnLibro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
    }

    public static ItemVenta crear(ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio subTotal) {
        validar(isbnLibro, cantidad, precioUnitario, subTotal);
        return new ItemVenta(UUID.randomUUID(), isbnLibro, cantidad, precioUnitario, subTotal);
    }

    private static void validar(ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio subTotal) {
        if (isbnLibro == null) {
            throw new CasillaVaciaException("El ISBN del libro no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new CasillaVaciaException("La cantidad del item debe ser mayor a cero.");
        }
        if (precioUnitario == null) {
            throw new CasillaVaciaException("El precio unitario no puede ser nulo.");
        }
        if (subTotal == null) {
            throw new CasillaVaciaException("El subtotal del item no puede ser nulo.");
        }
    }

    public void cambiarCantidad(int nuevaCantidad, Precio nuevoSubTotal) {
        if (nuevaCantidad <= 0) {
            throw new CasillaVaciaException("La cantidad del item debe ser mayor a cero.");
        }
        if (nuevoSubTotal == null) {
            throw new CasillaVaciaException("El subtotal del item no puede ser nulo.");
        }
        this.cantidad = nuevaCantidad;
        this.subTotal = nuevoSubTotal;
    }

    public void cambiarPrecioUnitario(Precio nuevoPrecioUnitario) {
        if (nuevoPrecioUnitario == null) {
            throw new CasillaVaciaException("El precio unitario no puede ser nulo.");
        }
        this.precioUnitario = nuevoPrecioUnitario;
    }

    public void actualizarSubTotal(Precio nuevoSubTotal) {
        if (nuevoSubTotal == null) {
            throw new CasillaVaciaException("El subtotal del item no puede ser nulo.");
        }
        this.subTotal = nuevoSubTotal;
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