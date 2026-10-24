
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompletarVentaCarrito {

    private final CarritoRepository carritoRepository;
    private final LibroRepository libroRepository;
    private final VerificadorDeCarrito verificadorDeCarrito;

    public CompletarVentaCarrito(
            CarritoRepository carritoRepository,
            LibroRepository libroRepository,
            VerificadorDeCarrito verificadorDeCarrito) {
        this.carritoRepository = carritoRepository;
        this.libroRepository = libroRepository;
        this.verificadorDeCarrito = verificadorDeCarrito;
    }

    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository
                .buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito no existe."
                        ));

        carrito.completarVentaCarrito();
        carrito.getItems()
                .forEach(itemCarrito -> {
                        String isbnLibro = itemCarrito.getIsbnLibro().isbn();
                        Libro libro = libroRepository.buscarPorISBN(isbnLibro).orElseThrow();
                        Stock stockNuevo = new Stock(libro.getStock().stock() - itemCarrito.getCantidad());
                        libro.actualizarStock(stockNuevo);
                        verificadorDeCarrito.actualizarStockItemsCarritoCreado(libro.getIsbn().isbn(), stockNuevo);
                });
        carritoRepository.guardarCarrito(carrito);
    }
}
