package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;

public class VerificadorDeCarrito {

    private final CarritoRepository carritoRepository;

    public VerificadorDeCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void verificarQueUsuarioNoTieneCarrito(String idUsuario) {

        boolean existeCarrito = carritoRepository
                .buscarPorIdUsuario(idUsuario)
                .isPresent();

        if (existeCarrito) {
            throw new ReglaDominioException(
                    "El usuario ya tiene un carrito registrado"
            );
        }
    }

    public void actualizarStockItemsCarritoCreado(String isbn, Stock stockNuevo) {
        carritoRepository
                .listarCarritosEnCreado()
                .forEach(carrito -> {
                    carrito.getItems().forEach(item -> {
                        if (item.getIsbnLibro().isbn().equals(isbn)) {
                            item.actualizarStock(stockNuevo);
                        }
                    });
                });
    }

    public void actualizarPrecioItemsCarritoCreado(String isbn, Precio precioNuevo) {
        carritoRepository
                .listarCarritosEnCreado()
                .forEach(carrito -> {
                    carrito.getItems().forEach(item -> {
                        if (item.getIsbnLibro().isbn().equals(isbn)) {
                            item.actualizarPrecio(precioNuevo);
                        }
                    });
                });
    }
}
