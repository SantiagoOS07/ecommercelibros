package com.uniquindio.ecommercelibros.domain.service;

public interface EncriptadorContrasenia {

    String encriptar(String contraseniaPlana);

    boolean coincide(String contraseniaPlana, String contraseniaHash);
}
