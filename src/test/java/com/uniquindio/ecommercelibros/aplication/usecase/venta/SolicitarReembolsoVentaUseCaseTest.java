package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.ItemVenta;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitarReembolsoVentaUseCaseTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private SolicitarReembolsoVentaUseCase useCase;

    @Test
    void deberiaSolicitarReembolsoConElMotivoYGuardar() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        Venta venta = mock(Venta.class);
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.of(venta));

        // Act
        useCase.ejecutar(idVenta, "El libro llego danado");

        // Assert
        InOrder orden = inOrder(venta, ventaRepository);
        orden.verify(venta).solicitarReembolsoVenta("El libro llego danado");
        orden.verify(ventaRepository).guardarVenta(venta);
    }

    @Test
    void deberiaReembolsarUnaVentaCanceladaReal() {
        // Arrange: Venta real, primero cancelada (CANCELADO -> REEMBOLSADO es valido)
        Venta venta = crearVentaReal();
        venta.cancelarVenta();
        when(ventaRepository.buscarVentaPorId(venta.getIdVenta())).thenReturn(Optional.of(venta));

        // Act
        useCase.ejecutar(venta.getIdVenta(), "Ya no lo necesito");

        // Assert
        assertEquals(EstadoPedido.REEMBOLSADO, venta.getEstadoPedido());
        verify(ventaRepository).guardarVenta(venta);
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaVentaNoExiste() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> useCase.ejecutar(idVenta, "motivo"));

        verify(ventaRepository, never()).guardarVenta(any());
    }

    @Test
    void noDeberiaGuardarCuandoElMotivoEsInvalido() {
        // Arrange: Venta real en PREPARANDO con motivo vacio
        Venta venta = crearVentaReal();
        when(ventaRepository.buscarVentaPorId(venta.getIdVenta())).thenReturn(Optional.of(venta));

        // Act & Assert
        assertThrows(CasillaVaciaException.class, () -> useCase.ejecutar(venta.getIdVenta(), ""));

        assertEquals(EstadoPedido.PREPARANDO, venta.getEstadoPedido());
        verify(ventaRepository, never()).guardarVenta(any());
    }

    @Test
    void noDeberiaGuardarCuandoLaVentaNoEsReembolsable() {
        // Arrange: una venta en PREPARANDO no puede pasar a REEMBOLSADO
        Venta venta = crearVentaReal();
        when(ventaRepository.buscarVentaPorId(venta.getIdVenta())).thenReturn(Optional.of(venta));

        // Act & Assert
        assertThrows(ReglaDominioException.class, () -> useCase.ejecutar(venta.getIdVenta(), "motivo"));

        assertEquals(EstadoPedido.PREPARANDO, venta.getEstadoPedido());
        verify(ventaRepository, never()).guardarVenta(any());
    }

    private Venta crearVentaReal() {
        ItemVenta item = ItemVenta.crear(
                new ISBN("9783161484100"),
                2,
                new Precio(25000, Moneda.COP),
                new Precio(50000, Moneda.COP)
        );
        return Venta.crear("usuario-1", List.of(item), new Precio(50000, Moneda.COP));
    }
}