package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolicitarReembolsoVentaUseCase {
    private final VentaRepository ventaRepository;

    @Transactional
    public void ejecutar(UUID idVenta, String motivo) {
        Venta venta = ventaRepository.buscarVentaPorId(idVenta).orElseThrow();
        venta.solicitarReembolsoVenta(motivo);
        ventaRepository.guardarVenta(venta);
    }
}
