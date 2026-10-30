package com.uniquindio.ecommercelibros.infrastructure.security;

import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("seguridad")
@RequiredArgsConstructor
public class SeguridadRecursos {
    private final CarritoRepository carritoRepository;
    private final VentaRepository ventaRepository;

    public boolean esDuenoDeCarrito(UUID idCarrito, Authentication auth) {
        return carritoRepository.buscarPorIdCarrito(idCarrito)
                .map(c -> c.getIdUsuario().equals(uid(auth)))
                .orElse(true);
    }

    public boolean esDuenoDeVenta(UUID idVenta, Authentication auth) {
        return ventaRepository.buscarVentaPorId(idVenta)
                .map(v -> v.getIdUsuario().equals(uid(auth)))
                .orElse(true);
    }

    private String uid(Authentication auth) {
        return auth.getPrincipal() instanceof Jwt jwt ? jwt.getClaimAsString("uid") : null;
    }
}