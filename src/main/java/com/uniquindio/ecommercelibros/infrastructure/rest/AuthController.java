package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.auth.LoginRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.auth.TokenResponse;
import com.uniquindio.ecommercelibros.infrastructure.security.AutenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticationService autenticationService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        return autenticationService.login(loginRequest);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> credencialesInvalidas() {
        return Map.of("error", "Usuario o contraseña incorrectos");
    }
}
