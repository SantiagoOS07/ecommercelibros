package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelarVentaUseCase {
    private final VentaRepository ventaRepository;

    @Transactional
    public void ejecutar(UUID idVenta) {
        Venta venta = ventaRepository.buscarVentaPorId(idVenta).orElseThrow();
        venta.cancelarVenta();
        ventaRepository.guardarVenta(venta);
    }
}
