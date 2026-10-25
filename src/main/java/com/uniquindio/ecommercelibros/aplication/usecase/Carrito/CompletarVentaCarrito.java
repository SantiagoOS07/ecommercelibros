
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CompletarVentaCarrito {

    private final CarritoRepository carritoRepository;
    private final LibroRepository libroRepository;
    private final VentaRepository ventaRepository;
    private final VerificadorDeCarrito verificadorDeCarrito;

    public CompletarVentaCarrito(
            CarritoRepository carritoRepository,
            LibroRepository libroRepository,
            VentaRepository ventaRepository,
            VerificadorDeCarrito verificadorDeCarrito) {
        this.carritoRepository = carritoRepository;
        this.libroRepository = libroRepository;
        this.ventaRepository = ventaRepository;
        this.verificadorDeCarrito = verificadorDeCarrito;
    }

    @Transactional
    public void ejecutar(UUID idCarrito) {

        Carrito carrito = carritoRepository
                .buscarPorIdCarrito(idCarrito)
                .orElseThrow(() ->
                        new ReglaDominioException(
                                "El carrito no existe."
                        ));

        Venta venta = carrito.completarVentaCarrito();

        carrito.getItems().forEach(itemCarrito -> {
            String isbnLibro = itemCarrito.getIsbnLibro().isbn();

            Libro libro = libroRepository.buscarPorISBN(isbnLibro)
                    .orElseThrow(() -> new ReglaDominioException(
                            "El libro con ISBN " + isbnLibro + " ya no existe."
                    ));

            // Se valida contra el stock REAL del libro, no contra el snapshot del carrito.
            int stockActual = libro.getStock().stock();
            if (stockActual < itemCarrito.getCantidad()) {
                throw new ReglaDominioException(
                        "Actualmente no hay suficiente stock del libro: " + isbnLibro
                                + ", stock disponible: " + stockActual
                );
            }

            Stock stockNuevo = new Stock(stockActual - itemCarrito.getCantidad());
            libro.actualizarStock(stockNuevo);

            if (stockNuevo.stock() == 0 && libro.getEstado() == EstadoLibro.DISPONIBLE) {
                libro.cambiarEstado(EstadoLibro.AGOTADO);
            }

            libroRepository.guardarLibro(libro);
            verificadorDeCarrito.actualizarStockItemsCarritoCreado(isbnLibro, stockNuevo);
        });

        ventaRepository.guardarVenta(venta);
        carritoRepository.guardarCarrito(carrito);
    }
}
