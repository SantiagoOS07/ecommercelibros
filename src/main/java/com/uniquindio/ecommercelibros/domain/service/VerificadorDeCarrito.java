package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
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
                    "El usuario ya tiene un carrito activo"
            );
        }
    }

    public void actualizarStockItemsCarritoCreado(String isbn, Stock stockNuevo) {
        ISBN isbnLibro = new ISBN(isbn);

        carritoRepository
                .listarCarritosEnCreado()
                .stream()
                .filter(carrito -> carrito.contieneLibro(isbnLibro))
                .forEach(carrito -> {
                    carrito.actualizarItemStock(isbnLibro, stockNuevo);
                    carritoRepository.guardarCarrito(carrito);
                });
    }

    public void actualizarPrecioItemsCarritoCreado(String isbn, Precio precioNuevo) {
        ISBN isbnLibro = new ISBN(isbn);

        carritoRepository
                .listarCarritosEnCreado()
                .stream()
                .filter(carrito -> carrito.contieneLibro(isbnLibro))
                .forEach(carrito -> {
                    carrito.actualizarItemPrecio(isbnLibro, precioNuevo);
                    carritoRepository.guardarCarrito(carrito);
                });
    }
}
