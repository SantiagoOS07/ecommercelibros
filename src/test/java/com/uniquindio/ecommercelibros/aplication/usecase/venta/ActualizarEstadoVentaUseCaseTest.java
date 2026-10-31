package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarEstadoVentaUseCaseTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private ActualizarEstadoVentaUseCase useCase;

    @Test
    void deberiaActualizarElEstadoYGuardarCuandoLaVentaExiste() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        Venta venta = mock(Venta.class);
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.of(venta));

        // Act
        useCase.ejecutar(idVenta);

        // Assert: primero se modifica el dominio y despues se guarda
        InOrder orden = inOrder(venta, ventaRepository);
        orden.verify(venta).actualizarEstadoPedido();
        orden.verify(ventaRepository).guardarVenta(venta);
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaVentaNoExiste() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> useCase.ejecutar(idVenta));

        verify(ventaRepository, never()).guardarVenta(any());
    }

    @Test
    void noDeberiaGuardarCuandoElDominioRechazaLaActualizacion() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        Venta venta = mock(Venta.class);
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.of(venta));
        doThrow(new ReglaDominioException("Estado no actualizable"))
                .when(venta).actualizarEstadoPedido();

        // Act & Assert
        assertThrows(ReglaDominioException.class, () -> useCase.ejecutar(idVenta));

        verify(ventaRepository, never()).guardarVenta(any());
    }
}
