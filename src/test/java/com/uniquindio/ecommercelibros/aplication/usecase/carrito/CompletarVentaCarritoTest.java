package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.CompletarVentaCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.exception.StockInsuficienteException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompletarVentaCarritoTest {

    private static final ISBN ISBN_LIBRO = new ISBN("9783161484100");

    private CarritoRepository carritoRepository;
    private LibroRepository libroRepository;
    private VentaRepository ventaRepository;
    private VerificadorDeCarrito verificadorDeCarrito;
    private CompletarVentaCarrito completarVenta;

    @BeforeEach
    void setUp() {
        carritoRepository = mock(CarritoRepository.class);
        libroRepository = mock(LibroRepository.class);
        ventaRepository = mock(VentaRepository.class);
        verificadorDeCarrito = mock(VerificadorDeCarrito.class);
        completarVenta = new CompletarVentaCarrito(
                carritoRepository, libroRepository, ventaRepository, verificadorDeCarrito);
    }

    @Test
    void conStockSuficienteDescuentaElStockYGuardaVentaYCarrito() {
        Carrito carrito = carritoProcesando();
        Libro libro = libro(10);
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));
        when(libroRepository.buscarPorISBN(ISBN_LIBRO.isbn())).thenReturn(Optional.of(libro));

        Venta venta = completarVenta.ejecutar(carrito.getIdCarrito());

        assertEquals(EstadoCarrito.VENDIDO, carrito.getEstado());
        assertEquals(new Stock(8), libro.getStock());
        verify(libroRepository).guardarLibro(libro);
        verify(ventaRepository).guardarVenta(venta);
        verify(carritoRepository).guardarCarrito(carrito);
    }

    @Test
    void conStockRealInsuficienteLanzaExcepcionYNoGuardaVentaNiCarrito() {
        Carrito carrito = carritoProcesando();
        Libro libro = libro(1);
        when(carritoRepository.buscarPorIdCarrito(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));
        when(libroRepository.buscarPorISBN(ISBN_LIBRO.isbn())).thenReturn(Optional.of(libro));

        assertThrows(StockInsuficienteException.class,
                () -> completarVenta.ejecutar(carrito.getIdCarrito()));

        verify(ventaRepository, never()).guardarVenta(any());
        verify(carritoRepository, never()).guardarCarrito(any());
    }

    private static Carrito carritoProcesando() {
        Carrito carrito = Carrito.crear("usuario-1");
        carrito.agregarItem(ISBN_LIBRO, 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.confirmarCarrito();
        return carrito;
    }

    private static Libro libro(int stock) {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", ISBN_LIBRO,
                "Una historia clásica", new NumeroPaginas(120), new Precio(20000, Moneda.COP),
                new Stock(stock), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA,
                "imagen.jpg", EstadoLibro.DISPONIBLE);
    }
}