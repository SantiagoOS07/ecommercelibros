package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActualizarEstadoVentaUseCase {
    private final VentaRepository ventaRepository;

    public void ejecutar(UUID idVenta) {
        Venta venta = ventaRepository.buscarVentaPorId(idVenta).orElseThrow();
        venta.actualizarEstadoPedido();
        ventaRepository.guardarVenta(venta);
    }
}
