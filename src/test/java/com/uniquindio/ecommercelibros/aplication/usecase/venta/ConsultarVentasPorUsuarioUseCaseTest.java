package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarVentasPorUsuarioUseCaseTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private ConsultarVentasPorUsuarioUseCase useCase;

    @Test
    void deberiaRetornarLasVentasDelUsuario() {
        // Arrange
        String idUsuario = "usuario-1";
        Venta venta1 = mock(Venta.class);
        Venta venta2 = mock(Venta.class);
        when(ventaRepository.buscarVentasPorIdUsuario(idUsuario))
                .thenReturn(List.of(venta1, venta2));

        // Act
        List<Venta> resultado = useCase.ejecutar(idUsuario);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(venta1));
        assertTrue(resultado.contains(venta2));
        verify(ventaRepository).buscarVentasPorIdUsuario(idUsuario);
    }

    @Test
    void deberiaRetornarListaVaciaCuandoElUsuarioNoTieneVentas() {
        // Arrange
        String idUsuario = "usuario-sin-ventas";
        when(ventaRepository.buscarVentasPorIdUsuario(idUsuario)).thenReturn(List.of());

        // Act
        List<Venta> resultado = useCase.ejecutar(idUsuario);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(ventaRepository).buscarVentasPorIdUsuario(idUsuario);
    }
}