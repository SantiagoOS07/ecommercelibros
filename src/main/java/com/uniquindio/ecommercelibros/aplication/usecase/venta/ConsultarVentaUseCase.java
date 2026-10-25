package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultarVentaUseCase {
    private final VentaRepository ventaRepository;

    @Transactional(readOnly = true)
    public Venta ejecutar(UUID idVenta) {
        return ventaRepository.buscarVentaPorId(idVenta).orElseThrow();
    }
}
