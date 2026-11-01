package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;

public class VerificadorDeUsuario {

    private final UsuarioRepository usuarioRepository;

    public VerificadorDeUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

   
    public void verificarQueCorreoNoExiste(String correo) {
        if (usuarioRepository.buscarPorCorreo(correo).isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un usuario registrado con ese correo");
        }
    }


    public void verificarQueUsuarioExisteYEstaActivo(String idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorIdUsuario(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id: " + idUsuario));

        if (usuario.getEstadoUsuario().estaInactivo()) {
            throw new ReglaDominioException("El usuario está inactivo");
        }
    }
}