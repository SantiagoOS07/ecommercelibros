package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;

public class VerificadorDeCarrito {

    private final CarritoRepository carritoRepository;

    public VerificadorDeCarrito(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public void verificarQueUsuarioNoTieneCarrito(String idUsuario) {

        boolean existeCarrito = carritoRepository
                .buscarPorIdUsuario(idUsuario)
                .isPresent();

        if (existeCarrito) {
            throw new ReglaDominioException(
                    "El usuario ya tiene un carrito registrado"
            );
        }
    }
}
