package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarVentaUseCaseTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private ConsultarVentaUseCase useCase;

    @Test
    void deberiaRetornarLaVentaCuandoExiste() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        Venta venta = mock(Venta.class);
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.of(venta));

        // Act
        Venta resultado = useCase.ejecutar(idVenta);

        // Assert
        assertSame(venta, resultado);
        verify(ventaRepository).buscarVentaPorId(idVenta);
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaVentaNoExiste() {
        // Arrange
        UUID idVenta = UUID.randomUUID();
        when(ventaRepository.buscarVentaPorId(idVenta)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> useCase.ejecutar(idVenta));

        verify(ventaRepository).buscarVentaPorId(idVenta);
    }
}