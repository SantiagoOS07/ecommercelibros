package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Carrito {

    private final UUID idCarrito;
    private final String idUsuario;
    private EstadoCarrito estado;
    private List<ItemCarrito> items; // La lista de items puede ser vacia

    private Carrito(String idUsuario) {
        this.idCarrito = UUID.randomUUID();
        this.idUsuario = idUsuario;
        this.estado = EstadoCarrito.CREADO;
        this.items = new ArrayList<>();
    }

    public static Carrito crear(String idUsuario) {
        verificar(idUsuario);
        return new Carrito(idUsuario);
    }

    private static void verificar(String idUsuario) {
        if (idUsuario == null || idUsuario.isEmpty()) {
            throw new ReglaDominioException("El id del usuario no puede ser nulo o vacio.");
        }
        if (!(verificarUsuario(idUsuario))) {
            throw new ReglaDominioException("El id del usuario no corresponde a un usuario real.");
        }
    }

    private static boolean verificarUsuario(String idUsuario) {

        // Aqui va la logica para si un idUsuario si le corresponde a un usuario real

        return false;
    }

    public void confirmarCarrito() {
        verificarCarritoNoVendido();
        verificarTransaccion(EstadoCarrito.PROCESANDO);
        this.estado = EstadoCarrito.PROCESANDO;
    }

    public void cancelarConfirmacionCarrito() {
        verificarCarritoNoVendido();
        if (this.estado != EstadoCarrito.PROCESANDO) {
            throw new ReglaDominioException("El carrito no puede cancelar la confirmación, ya que no se esta procesando.");
        }
        this.estado = EstadoCarrito.CREADO;
    }

    public void completarVentaCarrito() {

    }

    private void verificarTransaccion(EstadoCarrito estadoCarritoSiguiente) {
        if (!this.estado.puedeTransicionarA(estadoCarritoSiguiente)) {
            throw new ReglaDominioException("El carrito no puede transitar al estado " + estadoCarritoSiguiente);
        }
    }

    private void verificarCarritoNoVendido() {
        if (this.estado.esFinal()) {
            throw new ReglaDominioException("El carrito ya ha sido vendido.");
        }
    }

    public void agregarItem(ISBN isbnLibro, int cantidad, Precio precioLibro) {
        ItemCarrito itemCarrito = ItemCarrito.crear(isbnLibro, cantidad, precioLibro);
        this.items.add(itemCarrito);
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
