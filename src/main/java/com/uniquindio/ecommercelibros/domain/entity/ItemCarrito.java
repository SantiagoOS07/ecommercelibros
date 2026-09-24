package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class ItemCarrito {

    private final UUID idItemCarrito;
    private final ISBN isbnLibro;
    private final Precio precioUnitario;
    private final Stock stockLibro;
    private int cantidad;
    private Precio precioTotal;

    private ItemCarrito(ISBN isbnLibro, int cantidad, Precio precioLibro, Stock stockLibro) {
        this.idItemCarrito = UUID.randomUUID();
        this.isbnLibro = isbnLibro;
        this.cantidad = cantidad;
        this.stockLibro = stockLibro;
        this.precioUnitario = precioLibro;
        this.precioTotal = new Precio(precioLibro.monto() * cantidad, precioLibro.moneda());
    }

    public static ItemCarrito crear(ISBN isbnLibro, int cantidad, Precio precioLibro, Stock stock) {
        verificar(isbnLibro, cantidad, precioLibro, stock);
        return new ItemCarrito(isbnLibro, cantidad, precioLibro, stock);
    }

    private static void verificar(ISBN isbnLibro, int cantidad, Precio precioLibro, Stock stockLibro) {
        if (isbnLibro == null) {
            throw new ReglaDominioException("El ISBN del libro no puede ser nulo o vacío.");
        }
        if (cantidad <= 0) {
            throw new ReglaDominioException("La cantidad del libro no puede ser menor o igual a cero.");
        }
        if (precioLibro == null) {
            throw new ReglaDominioException("El precio del libro no puede ser nulo.");
        }
        if (stockLibro == null) {
            throw new ReglaDominioException("El stock del libro no puede ser nulo.");
        }
        if (cantidad > stockLibro.stock()) {
            throw new ReglaDominioException("La cantidad del libro no puede ser mayor al stock disponible.");
        }
    }

    public void actualizarCantidad(int cantidad, Stock stockLibro) {
        if (cantidad > stockLibro.stock()) {
            throw new ReglaDominioException("La cantidad del libro no puede ser mayor al stock disponible.");
        }
        this.cantidad = cantidad;
        this.precioTotal = new Precio(precioUnitario.monto() * cantidad, precioUnitario.moneda());
    }

    public void actualizarPrecio(Precio nuevoPrecioLibro) {
        if (nuevoPrecioLibro == null) {
            throw new ReglaDominioException("El precio del libro no puede ser nulo");
        }
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
