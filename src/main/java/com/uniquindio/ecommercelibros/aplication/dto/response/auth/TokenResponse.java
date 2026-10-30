package com.uniquindio.ecommercelibros.aplication.dto.response.auth;

import java.time.Instant;
import java.util.Collection;

public record TokenResponse (String token, String type, Instant expireAt, Collection<String> roles){
}
