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

    public void verificarQueLibroNoEstaEnCarritoProcesando(String isbn) {
        ISBN isbnLibro = new ISBN(isbn);
        boolean enProceso = carritoRepository.listarCarritosEnProcesando().stream()
                .anyMatch(c -> c.contieneLibro(isbnLibro));
        if (enProceso) {
            throw new ReglaDominioException(
                    "No se puede eliminar el libro: está en un carrito que se está procesando");
        }
    }

    public void retirarLibroDeCarritosCreado(String isbn) {
        ISBN isbnLibro = new ISBN(isbn);
        carritoRepository.listarCarritosEnCreado().stream()
                .filter(c -> c.contieneLibro(isbnLibro))
                .forEach(c -> {
                    c.eliminarItem(isbnLibro);
                    carritoRepository.guardarCarrito(c);
                });
    }
}
