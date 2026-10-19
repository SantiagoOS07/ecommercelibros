
package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarritoRepository {

    Optional<Carrito> buscarPorIdCarrito(UUID idCarrito);

    Optional<Carrito> buscarPorIdUsuario(String idUsuario);

    List<Carrito> listarTodos();

    void guardarCarrito(Carrito carrito);

    void eliminarCarrito(UUID idCarrito);
}
