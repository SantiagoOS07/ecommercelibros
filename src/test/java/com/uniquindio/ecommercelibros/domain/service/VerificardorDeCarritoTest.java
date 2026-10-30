package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificadorDeCarritoTest {

    private static final ISBN ISBN_A = new ISBN("9783161484100");
    private static final ISBN ISBN_B = new ISBN("9789587580188");

    @Mock private CarritoRepository carritoRepository;
    private VerificadorDeCarrito verificador;

    @BeforeEach
    void setUp() {
        verificador = new VerificadorDeCarrito(carritoRepository);
    }

    @Test
    void verificarQueUsuarioNoTieneCarrito_conCarritoActivo_lanzaDuplicado() { // [M-19]
        when(carritoRepository.buscarPorIdUsuario("u1")).thenReturn(Optional.of(Carrito.crear("u1")));

        assertThrows(RecursoDuplicadoException.class, () -> verificador.verificarQueUsuarioNoTieneCarrito("u1"));
    }

    @Test
    void verificarQueUsuarioNoTieneCarrito_sinCarrito_noLanza() {
        when(carritoRepository.buscarPorIdUsuario("u1")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> verificador.verificarQueUsuarioNoTieneCarrito("u1"));
    }

    @Test
    void actualizarPrecioItemsCarritoCreado_actualizaSoloLosCarritosQueContienenElLibro() {
        Carrito conLibro = carritoCon("u1", ISBN_A);
        Carrito sinLibro = carritoCon("u2", ISBN_B);
        when(carritoRepository.listarCarritosEnCreado()).thenReturn(List.of(conLibro, sinLibro));

        verificador.actualizarPrecioItemsCarritoCreado(ISBN_A.isbn(), new Precio(30000, Moneda.COP));

        assertEquals(new Precio(60000, Moneda.COP), conLibro.getItems().getFirst().getPrecioTotal());
        assertEquals(new Precio(40000, Moneda.COP), sinLibro.getItems().getFirst().getPrecioTotal());
        verify(carritoRepository).guardarCarrito(conLibro);
        verify(carritoRepository, never()).guardarCarrito(sinLibro);
    }

    @Test
    void actualizarStockItemsCarritoCreado_actualizaSoloLosCarritosQueContienenElLibro() {
        Carrito conLibro = carritoCon("u1", ISBN_A);
        Carrito sinLibro = carritoCon("u2", ISBN_B);
        when(carritoRepository.listarCarritosEnCreado()).thenReturn(List.of(conLibro, sinLibro));

        verificador.actualizarStockItemsCarritoCreado(ISBN_A.isbn(), new Stock(5));

        assertEquals(new Stock(5), conLibro.getItems().getFirst().getStockLibro());
        assertEquals(new Stock(10), sinLibro.getItems().getFirst().getStockLibro());
        verify(carritoRepository).guardarCarrito(conLibro);
        verify(carritoRepository, never()).guardarCarrito(sinLibro);
    }

    @Test
    void retirarLibroDeCarritosCreado_quitaElItemYGuarda() {
        Carrito conLibro = carritoCon("u1", ISBN_A);
        Carrito sinLibro = carritoCon("u2", ISBN_B);
        when(carritoRepository.listarCarritosEnCreado()).thenReturn(List.of(conLibro, sinLibro));

        verificador.retirarLibroDeCarritosCreado(ISBN_A.isbn());

        assertTrue(conLibro.getItems().isEmpty());
        assertEquals(1, sinLibro.getItems().size());
        verify(carritoRepository).guardarCarrito(conLibro);
        verify(carritoRepository, never()).guardarCarrito(sinLibro);
    }

    @Test
    void verificarQueLibroNoEstaEnCarritoProcesando_conCarritoProcesando_lanza() {
        Carrito procesando = carritoCon("u1", ISBN_A);
        procesando.confirmarCarrito();
        when(carritoRepository.listarCarritosEnProcesando()).thenReturn(List.of(procesando));

        assertThrows(ReglaDominioException.class,
                () -> verificador.verificarQueLibroNoEstaEnCarritoProcesando(ISBN_A.isbn()));
    }

    @Test
    void verificarQueLibroNoEstaEnCarritoProcesando_sinCarritosProcesando_noLanza() {
        when(carritoRepository.listarCarritosEnProcesando()).thenReturn(List.of());

        assertDoesNotThrow(() -> verificador.verificarQueLibroNoEstaEnCarritoProcesando(ISBN_A.isbn()));
    }

    private static Carrito carritoCon(String idUsuario, ISBN isbn) {
        Carrito carrito = Carrito.crear(idUsuario);
        carrito.agregarItem(isbn, 2, new Precio(20000, Moneda.COP), new Stock(10));
        return carrito;
    }
}