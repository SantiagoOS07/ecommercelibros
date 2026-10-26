package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Eliminación lógica (soft delete): el usuario queda INACTIVO, nunca se borra de la base de datos. */
@Service
@Transactional
public class EliminarUsuario {

    private final UsuarioRepository usuarioRepository;

    public EliminarUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void eliminar(String idUsuario) {

        Usuario usuario = usuarioRepository.buscarPorIdUsuario(idUsuario)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El usuario no existe"));

        usuario.desactivar();

        usuarioRepository.guardarUsuario(usuario);
    }
}