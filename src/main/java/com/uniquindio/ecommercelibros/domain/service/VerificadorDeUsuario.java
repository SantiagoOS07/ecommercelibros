package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;

public class VerificadorDeUsuario {

    private final UsuarioRepository usuarioRepository;

    public VerificadorDeUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void verificarQueCorreoNoExiste(String correo) {

        boolean existeCorreo = usuarioRepository
                .buscarPorCorreo(correo)
                .isPresent();

        if (existeCorreo) {
            throw new ReglaDominioException(
                    "Ya existe un usuario registrado con ese correo"
            );
        }
    }
}