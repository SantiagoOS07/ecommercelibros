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
    private final String idUsuario;
    private final List<ItemVenta> items;
    private final LocalDate fecha;
    private final Precio total;
    private EstadoPedido estadoPedido;
    private static final int DIAS_MAXIMOS_REEMBOLSO = 30;

    private Venta(UUID idVenta, String idUsuario, List<ItemVenta> items, Precio total, LocalDate fecha, EstadoPedido estadoPedido) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.items = items;
        this.total = total;
        this.fecha = fecha;
        this.estadoPedido = estadoPedido;
    }

    public static Venta crear(String idUsuario, List<ItemVenta> items, Precio total) {
        validar(idUsuario, items, total);
        return new Venta(UUID.randomUUID(), idUsuario, new ArrayList<>(items), total, LocalDate.now(), EstadoPedido.PREPARANDO);
    }

    private static void validar(String idUsuario,List<ItemVenta> items, Precio total) {
        if (idUsuario == null || idUsuario.isEmpty()) {
            throw new CasillaVaciaException("El id del usuario no puede ser nulo o vacío.");
        }
        if (items == null || items.isEmpty()) {
            throw new CasillaVaciaException("La venta debe tener al menos un item.");
        }
        if (total == null) {
            throw new CasillaVaciaException("El total de la venta no puede ser nulo.");
        }
    }

    public static Venta reconstruir(UUID idVenta, String idUsuario, List<ItemVenta> items, Precio total, LocalDate fecha, EstadoPedido estadoPedido) {
        return new Venta(idVenta, idUsuario, new ArrayList<>(items), total, fecha, estadoPedido);
    }

    public void cancelarVenta() {
        if (!this.estadoPedido.puedeTransicionarA(EstadoPedido.CANCELADO)) {
            throw new ReglaDominioException("La venta solo puede cancelarse si está en estado PREPARANDO.");
        }
        this.estadoPedido = EstadoPedido.CANCELADO;
    }

    public void solicitarReembolsoVenta(String motivo) {
        verificarMotivo(motivo);
        verificarVentaReembolsable();
        if (estadoPedido.fueCancelado()) {
            this.estadoPedido = EstadoPedido.REEMBOLSADO;
        }
        else if (estadoPedido.fueEntregado()) {
            LocalDate limite = LocalDate.now().minusDays(DIAS_MAXIMOS_REEMBOLSO);
            if (fecha.isBefore(limite)) {
                throw new ReglaDominioException("El tiempo máximo de reembolso ha expirado. El reembolso ya no está disponible.");
            }
            this.estadoPedido = EstadoPedido.REEMBOLSADO;
        }
    }

    private void verificarMotivo(String motivo) {
        if (motivo == null || motivo.isEmpty()) {
            throw new CasillaVaciaException("El motivo del reembolso no puede ser nulo o vacío.");
        }
    }

    private void verificarVentaReembolsable() {
        if (!this.estadoPedido.puedeTransicionarA(EstadoPedido.REEMBOLSADO)) {
            throw new ReglaDominioException("La venta solo puede pedir reembolso si está en estado CANCELADO o ENTREGADO.");
        }
    }

    public void actualizarEstadoPedido() {
        verificarActualizacionEstado();
        if (this.estadoPedido.estaEnProceso()) {
            this.estadoPedido = EstadoPedido.ENVIADO;
        }
        else if (this.estadoPedido.fueEnviado()) {
            this.estadoPedido = EstadoPedido.ENTREGADO;
        }
    }

    private void verificarActualizacionEstado() {
        if (!(this.estadoPedido.estaEnProceso() || this.estadoPedido.fueEnviado())) {
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