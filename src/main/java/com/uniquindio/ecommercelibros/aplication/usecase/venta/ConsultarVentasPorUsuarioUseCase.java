package com.uniquindio.ecommercelibros.aplication.usecase.venta;

import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarVentasPorUsuarioUseCase {
    private final VentaRepository ventaRepository;

    public List<Venta> ejecutar(String idUsuario) {
        return ventaRepository.buscarVentasPorIdUsuario(idUsuario);
    }
}
