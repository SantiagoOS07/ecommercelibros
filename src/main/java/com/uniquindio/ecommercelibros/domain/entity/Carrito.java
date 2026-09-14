package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Carrito {

    private final UUID idCarrito;
    private String idUsuario;
    private List<ItemCarrito> items;
    private EstadoCarrito estado;

    public Carrito(UUID idCarrito, String idUsuario, List<ItemCarrito> items, EstadoCarrito estado) {
        this.idCarrito = UUID.randomUUID();
        this.idUsuario = idUsuario;
        this.items = items;
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Carrito carrito = (Carrito) o;
        return Objects.equals(idCarrito, carrito.idCarrito);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCarrito);
    }
}
