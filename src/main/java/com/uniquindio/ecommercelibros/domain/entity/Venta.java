package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

public class Venta {

    private final UUID idVenta;
    private final List<ItemVenta> items;
    private LocalDate fecha;
    private final Precio total;
    private EstadoPedido estadoPedido;

    public Venta(UUID idVenta, List<ItemVenta> items, Precio total, LocalDate fecha, EstadoPedido estadoPedido) {
        this.idVenta = UUID.randomUUID();
        this.items = items;
        this.total = total;
        this.fecha = LocalDate.now();
        this.estadoPedido = EstadoPedido.EN_PROCESO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta venta)) return false;
        return idVenta.equals(venta.idVenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta);
    }
}