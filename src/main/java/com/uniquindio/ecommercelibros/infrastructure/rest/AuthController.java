package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.auth.LoginRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.auth.TokenResponse;
import com.uniquindio.ecommercelibros.infrastructure.security.AutenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticationService autenticationService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return autenticationService.login(loginRequest);
    }
}
