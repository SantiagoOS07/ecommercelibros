
package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Carrito {

    private final UUID idCarrito;
    private final String idUsuario;
    private EstadoCarrito estado;
    private List<ItemCarrito> items;
    private Precio precioTotal;

    private Carrito(String idUsuario) {
        this(
                UUID.randomUUID(),
                idUsuario,
                EstadoCarrito.CREADO,
                new ArrayList<>()
        );
    }

    private Carrito(
            UUID idCarrito,
            String idUsuario,
            EstadoCarrito estado,
            List<ItemCarrito> items) {

        verificar(idUsuario);

        this.idCarrito = Objects.requireNonNull(
                idCarrito,
                "El id del carrito es obligatorio."
        );
        this.idUsuario = idUsuario;
        this.estado = Objects.requireNonNull(
                estado,
                "El estado del carrito es obligatorio."
        );
        this.items = new ArrayList<>(
                Objects.requireNonNull(
                        items,
                        "La lista de artículos es obligatoria."
                )
        );

        this.precioTotal = calcularPrecioTotal();
    }

    public static Carrito crear(String idUsuario) {
        verificar(idUsuario);
        return new Carrito(idUsuario);
    }

    public static Carrito reconstruir(
            UUID idCarrito,
            String idUsuario,
            EstadoCarrito estado,
            List<ItemCarrito> items,
            double precioTotal) {

        return new Carrito(
                idCarrito,
                idUsuario,
                estado,
                items
        );
    }

    private static void verificar(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new ReglaDominioException(
                    "El id del usuario no puede ser nulo o vacío."
            );
        }
    }

    public void confirmarCarrito() {
        verificarCarritoNoVendido();
        verificarTransaccion(EstadoCarrito.PROCESANDO);
        verificarStockDisponible();
        this.estado = EstadoCarrito.PROCESANDO;
    }

    public void cancelarConfirmacionCarrito() {
        verificarCarritoNoVendido();

        if (this.estado != EstadoCarrito.PROCESANDO) {
            throw new ReglaDominioException(
                    "El carrito no puede cancelar la confirmación, " +
                            "ya que no se está procesando."
            );
        }

        this.estado = EstadoCarrito.CREADO;
    }

    public void completarVentaCarrito() {
        verificarCarritoNoVendido();

        if (this.estado != EstadoCarrito.PROCESANDO) {
            throw new ReglaDominioException(
                    "El carrito no puede completar la venta, " +
                            "ya que no se está procesando."
            );
        }

        if (this.items.isEmpty()) {
            throw new ReglaDominioException(
                    "El carrito no puede completar la venta, " +
                            "ya que no tiene artículos."
            );
        }

        verificarTransaccion(EstadoCarrito.VENDIDO);
        Precio totalVenta = calcularPrecioTotal();
        List<ItemVenta> itemsVenta = procesarItemsCarrito();

        Venta.crear(itemsVenta, totalVenta);

        this.precioTotal = totalVenta;
        this.estado = EstadoCarrito.VENDIDO;
    }

    public Precio calcularPrecioTotal() {
        double total = items.stream()
                .map(ItemCarrito::getPrecioTotal)
                .mapToDouble(this::convertirAUSD)
                .sum();

        return new Precio(total, Moneda.USD);
    }

    private void verificarStockDisponible() {
        this.items.forEach(item -> {
            if (item.getStockLibro().stock() < item.getCantidad()) {
                throw new ReglaDominioException(
                        "Actualmente no hay suficiente stock del libro: " + item.getIsbnLibro() + ", stock disponible: " + item.getStockLibro().stock()
                );
            }
        });
    }

    private List<ItemVenta> procesarItemsCarrito() {
        return this.items.stream()
                .map(item -> ItemVenta.crear(
                        item.getIsbnLibro(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getPrecioTotal()
                ))
                .toList();
    }

    private double convertirAUSD(Precio precio) {
        return switch (precio.moneda()) {
            case COP -> precio.monto() / precio.moneda().equivalencia;
            case USD -> precio.monto();
        };
    }

    private void actualizarPrecioTotal() {
        this.precioTotal = calcularPrecioTotal();
    }

    private void verificarTransaccion(
            EstadoCarrito estadoCarritoSiguiente) {

        if (!this.estado.puedeTransicionarA(estadoCarritoSiguiente)) {
            throw new ReglaDominioException(
                    "El carrito no puede transitar al estado " +
                            estadoCarritoSiguiente
            );
        }
    }

    private void verificarCarritoNoVendido() {
        if (this.estado.esFinal()) {
            throw new ReglaDominioException(
                    "El carrito ya ha sido vendido."
            );
        }
    }

    private void verificarCarritoEditable() {
        if (this.estado != EstadoCarrito.CREADO) {
            throw new ReglaDominioException(
                    "El carrito no se puede modificar " +
                            "porque no está en estado CREADO."
            );
        }
    }

    public void agregarItem(
            ISBN isbnLibro,
            int cantidad,
            Precio precioLibro,
            Stock stock) {

        verificarCarritoEditable();

        ItemCarrito itemCarrito = ItemCarrito.crear(
                isbnLibro,
                cantidad,
                precioLibro,
                stock
        );

        this.items.add(itemCarrito);
        actualizarPrecioTotal();
    }

    public void eliminarItem(ISBN isbnLibro) {
        verificarCarritoEditable();

        boolean eliminado = this.items.removeIf(
                item -> item.getIsbnLibro().equals(isbnLibro)
        );

        if (eliminado) {
            actualizarPrecioTotal();
        }
    }

    public void actualizarItemCantidad(
            ISBN isbnLibro,
            int nuevaCantidad,
            Stock stockLibro) {

        verificarCarritoEditable();

        if (nuevaCantidad < 0) {
            throw new ReglaDominioException(
                    "La cantidad debe ser mayor o igual a cero."
            );
        }

        if (nuevaCantidad == 0) {
            eliminarItem(isbnLibro);
            return;
        }

        ItemCarrito item = this.items.stream()
                .filter(i -> i.getIsbnLibro().equals(isbnLibro))
                .findFirst()
                .orElseThrow(() -> new ReglaDominioException(
                        "El libro no se encuentra en el carrito."
                ));

        item.actualizarCantidad(nuevaCantidad, stockLibro);

        actualizarPrecioTotal();
    }

    public void actualizarItemPrecio(
            ISBN isbnLibro,
            Precio nuevoPrecio) {

        verificarCarritoEditable();

        if (nuevoPrecio == null) {
            throw new ReglaDominioException(
                    "El nuevo precio no puede ser nulo."
            );
        }

        int indice = -1;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIsbnLibro().equals(isbnLibro)) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            throw new ReglaDominioException(
                    "El libro no se encuentra en el carrito."
            );
        }

        ItemCarrito itemAnterior = items.get(indice);

        ItemCarrito itemActualizado = ItemCarrito.crear(
                itemAnterior.getIsbnLibro(),
                itemAnterior.getCantidad(),
                nuevoPrecio,
                itemAnterior.getStockLibro()
        );

        items.set(indice, itemActualizado);

        actualizarPrecioTotal();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Carrito carrito = (Carrito) o;
        return Objects.equals(idCarrito, carrito.idCarrito);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCarrito);
    }
}
