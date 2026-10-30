
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
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
                        new RecursoNoEncontradoException(
                                "El carrito no existe."
                        ));

        Venta venta = carrito.completarVentaCarrito();

        carrito.getItems().forEach(itemCarrito -> {
            String isbnLibro = itemCarrito.getIsbnLibro().isbn();

            Libro libro = libroRepository.buscarPorISBN(isbnLibro)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "El libro con ISBN " + isbnLibro + " ya no existe."
                    ));
            libro.descontarStock(itemCarrito.getCantidad());
            libroRepository.guardarLibro(libro);
            verificadorDeCarrito.actualizarStockItemsCarritoCreado(isbnLibro, libro.getStock());
        });

        ventaRepository.guardarVenta(venta);
        carritoRepository.guardarCarrito(carrito);
    }
}
