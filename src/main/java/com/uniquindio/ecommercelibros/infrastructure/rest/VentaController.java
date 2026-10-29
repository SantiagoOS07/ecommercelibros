package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.venta.SolicitarReembolsoVentaRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.venta.VentaResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.venta.*;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.VentaMapper;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final ActualizarEstadoVentaUseCase actualizarEstadoVentaUseCase;
    private final CancelarVentaUseCase cancelarVentaUseCase;
    private final ConsultarVentasPorUsuarioUseCase consultarVentasPorUsuarioUseCase;
    private final ConsultarVentaUseCase consultarVentaUseCase;
    private final SolicitarReembolsoVentaUseCase solicitarReembolsoVentaUseCase;
    private final VentaMapper mapper;

    @GetMapping("/{idVenta}")
    public VentaResponse consultarVenta(@PathVariable("idVenta") UUID idVenta) {
        return mapper.toResponseVenta(consultarVentaUseCase.ejecutar(idVenta));
    }

    @GetMapping
    public List<VentaResponse> consultarVentasPorUsuario(@RequestParam("idUsuario") String idUsuario) {
        return mapper.toResponseVentas(consultarVentasPorUsuarioUseCase.ejecutar(idUsuario));
    }

    @PutMapping("/{idVenta}/estado")
    public ResponseEntity<Void> actualizarEstadoVenta(@PathVariable("idVenta") UUID idVenta) {
        actualizarEstadoVentaUseCase.ejecutar(idVenta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idVenta}/cancelar")
    public ResponseEntity<Void> cancelarVenta(@PathVariable("idVenta") UUID idVenta) {
        cancelarVentaUseCase.ejecutar(idVenta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idVenta}/reembolso")
    public ResponseEntity<Void> solicitarReembolsoVenta(@PathVariable("idVenta") UUID idVenta,
                                                        @Valid @RequestBody SolicitarReembolsoVentaRequest request) {
        solicitarReembolsoVentaUseCase.ejecutar(idVenta, request.motivo());
        return ResponseEntity.noContent().build();
    }
}
