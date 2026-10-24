package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

@Getter
public class Venta {

    private final UUID idVenta;
    private final List<ItemVenta> items;
    private final LocalDate fecha;
    private final Precio total;
    private EstadoPedido estadoPedido;
    private final int DIAS_MAXIMOS_REEMBOLSO = 30;

    private Venta(UUID idVenta, List<ItemVenta> items, Precio total, LocalDate fecha, EstadoPedido estadoPedido) {
        this.idVenta = idVenta;
        this.items = items;
        this.total = total;
        this.fecha = fecha;
        this.estadoPedido = estadoPedido;
    }

    public static Venta crear(List<ItemVenta> items, Precio total) {
        validar(items, total);
        return new Venta(UUID.randomUUID(), new ArrayList<>(items), total, LocalDate.now(), EstadoPedido.PREPARANDO);
    }

    private static void validar(List<ItemVenta> items, Precio total) {
        if (items == null || items.isEmpty()) {
            throw new CasillaVaciaException("La venta debe tener al menos un item.");
        }
        if (total == null) {
            throw new CasillaVaciaException("El total de la venta no puede ser nulo.");
        }
    }

    public void cancelarVenta() {
        if (!this.estadoPedido.puedeTransicionarA(EstadoPedido.CANCELADO)) {
            throw new ReglaDominioException("La venta solo puede cancelarse si está en estado PREPARANDO.");
        }
        this.estadoPedido = EstadoPedido.CANCELADO;
    }

    public void solicitarReembolsoVenta(String motivo) {
        if (motivo == null || motivo.isEmpty()) {
            throw new CasillaVaciaException("El motivo del reembolso no puede ser nulo o vacío.");
        }
        if (!this.estadoPedido.puedeTransicionarA(EstadoPedido.REEMBOLSADO)) {
            throw new ReglaDominioException("La venta solo puede pedir reembolso si está en estado CANCELADO o ENTREGADO.");
        }
        if (estadoPedido.fueCancelado()) {
            this.estadoPedido = EstadoPedido.REEMBOLSADO;
        }
        if (estadoPedido.fueEntregado()) {
            if (fecha.isBefore(LocalDate.now().minusDays(DIAS_MAXIMOS_REEMBOLSO))) {
                this.estadoPedido = EstadoPedido.REEMBOLSADO;
            } else {
                throw new ReglaDominioException("El tiempo maximo de reembolso ha expirado. El reembolso ya no está disponible.");
            }
        }
    }

    public void actualizarEstadoPedido() {
        if (this.estadoPedido.estaEnProceso()) {
            this.estadoPedido = EstadoPedido.ENVIADO;
        }
        if (this.estadoPedido.fueEnviado()) {
            this.estadoPedido = EstadoPedido.ENTREGADO;
        }
        else {
            throw new ReglaDominioException("El estado del pedido solo se puede actualizar si está en estado PREPARANDO o ENVIADO.");
        }
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