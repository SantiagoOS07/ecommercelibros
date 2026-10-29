package com.uniquindio.ecommercelibros.infrastructure.security;

import com.uniquindio.ecommercelibros.domain.service.EncriptadorContrasenia;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderAdapter implements EncriptadorContrasenia {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptar(String contraseniaPlana) {
        return passwordEncoder.encode(contraseniaPlana);
    }

    @Override
    public boolean coincide(String contraseniaPlana, String contraseniaHash) {
        return passwordEncoder.matches(contraseniaPlana, contraseniaHash);
    }
}
