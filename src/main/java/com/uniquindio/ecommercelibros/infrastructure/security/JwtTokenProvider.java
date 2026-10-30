package com.uniquindio.ecommercelibros.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtEncoder encoder;

    public String generar(String correo, String idUsuario, Collection<String> roles, Instant emitidoEn, Instant expiraEn){

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(JwtConfig.ISSUER)
                .issuedAt(emitidoEn)
                .expiresAt(expiraEn)
                .subject(correo)
                .claim("uid", idUsuario)
                .claim("roles", roles)
                .build();

        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
