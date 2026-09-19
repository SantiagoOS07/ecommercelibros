package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Venta {

    private final UUID idVenta;
    private final List<ItemVenta> items;
    private LocalDate fecha;
    private Precio total;
    private EstadoPedido estadoPedido;

    private Venta(UUID idVenta, List<ItemVenta> items, Precio total, LocalDate fecha, EstadoPedido estadoPedido) {
        this.idVenta = idVenta;
        this.items = items;
        this.total = total;
        this.fecha = fecha;
        this.estadoPedido = estadoPedido;
    }

    public static Venta crear(List<ItemVenta> items, Precio total) {
        validar(items, total);
        return new Venta(UUID.randomUUID(), new ArrayList<>(items), total, LocalDate.now(), EstadoPedido.EN_PROCESO);
    }

    private static void validar(List<ItemVenta> items, Precio total) {
        if (items == null || items.isEmpty()) {
            throw new CasillaVaciaException("La venta debe tener al menos un item.");
        }
        if (total == null) {
            throw new CasillaVaciaException("El total de la venta no puede ser nulo.");
        }
    }

    public void agregarItem(ItemVenta item) {
        if (item == null) {
            throw new CasillaVaciaException("El item a agregar no puede ser nulo.");
        }
        if (this.items.contains(item)) {
            throw new ReglaDominioException("El item ya se encuentra registrado en la venta.");
        }
        this.items.add(item);
    }

    public void eliminarItem(ItemVenta item) {
        if (item == null || !this.items.contains(item)) {
            throw new ReglaDominioException("El item seleccionado no pertenece a la venta.");
        }
        if (this.items.size() == 1) {
            throw new CasillaVaciaException("La venta debe conservar al menos un item.");
        }
        this.items.remove(item);
    }

    public void actualizarTotal(Precio nuevoTotal) {
        if (nuevoTotal == null) {
            throw new CasillaVaciaException("El total de la venta no puede ser nulo.");
        }
        this.total = nuevoTotal;
    }

    public void cambiarFecha(LocalDate nuevaFecha) {
        if (nuevaFecha == null) {
            throw new CasillaVaciaException("La fecha de la venta no puede ser nula.");
        }
        this.fecha = nuevaFecha;
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new CasillaVaciaException("El estado del pedido no puede ser nulo.");
        }
        this.estadoPedido = nuevoEstado;
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