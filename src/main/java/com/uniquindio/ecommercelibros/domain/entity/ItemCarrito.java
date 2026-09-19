package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
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
        if (isbnLibro.isbn() == null || isbnLibro.isbn().isEmpty()) {
            throw new ReglaDominioException("El ISBN del libro no puede ser nulo o vacío.");
        }
        if (!(verificarISBN(isbnLibro))) {
            throw new ReglaDominioException("El ISBN del libro no corresponde a un libro real.");
        }
        if (cantidad <= 0) {
            throw new ReglaDominioException("La cantidad del libro no puede ser menor o igual a cero.");
        }
        if (cantidad > obtenerStockLibro(isbnLibro)) {
            throw new ReglaDominioException("La cantidad del libro no puede ser mayor al stock disponible.");
        }
        if (precioLibro == null) {
            throw new ReglaDominioException("El precio del libro no puede ser nulo.");
        }
    }

    private static int obtenerStockLibro(ISBN isbnLibro) {

        // Aqui va la logica para obtener el stock del libro segun el isbnLibro

        return 0;
    }

    private static boolean verificarISBN(ISBN isbnLibro) {

        // Aqui va la logica para si un isbnLibro si le corresponde a un libro real

        return false;
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
