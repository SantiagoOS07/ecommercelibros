package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Venta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VentaRepository {
    void guardarVenta(Venta venta);
    Optional<Venta> buscarVentaPorId(UUID id);
    List<Venta> buscarVentasPorIdUsuario(String idUsuario);
}
