package com.uniquindio.ecommercelibros.infrastructure.security;

import com.uniquindio.ecommercelibros.aplication.dto.request.auth.LoginRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.auth.TokenResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiry}")
    private long minutosDeVida;

    public TokenResponse login(LoginRequest request) {
        Authentication autenticacion = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contrasenia()));

        CustomUserDetails detalles = (CustomUserDetails) autenticacion.getPrincipal();

        List<String> roles = List.of(detalles.usuarioEntity().getRolUsuario().name());

        Instant ahora = Instant.now();
        Instant expira = ahora.plus(minutosDeVida, ChronoUnit.MINUTES);

        String token = jwtTokenProvider.generar(
                autenticacion.getName(), detalles.usuarioEntity().getIdUsuario(), roles, ahora, expira);

        return new TokenResponse(token, "Bearer", expira, roles);
    }

}
