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
    private final ISBN isbnLibro;
    private final int cantidad;
    private final Precio precioUnitario;
    private final Precio precioTotal;

    private ItemVenta(UUID idItemVenta, ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio precioTotal) {
        this.idItemVenta = idItemVenta;
        this.isbnLibro = isbnLibro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

    public static ItemVenta crear(ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio precioTotal) {
        validar(isbnLibro, cantidad, precioUnitario, precioTotal);
        return new ItemVenta(UUID.randomUUID(), isbnLibro, cantidad, precioUnitario, precioTotal);
    }

    private static void validar(ISBN isbnLibro, int cantidad, Precio precioUnitario, Precio precioTotal) {
        if (isbnLibro == null) {
            throw new CasillaVaciaException("El ISBN del libro no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new CasillaVaciaException("La cantidad del item debe ser mayor a cero.");
        }
        if (precioUnitario == null) {
            throw new CasillaVaciaException("El precio unitario no puede ser nulo.");
        }
        if (precioTotal == null) {
            throw new CasillaVaciaException("El precio total no puede ser nulo.");
        }
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