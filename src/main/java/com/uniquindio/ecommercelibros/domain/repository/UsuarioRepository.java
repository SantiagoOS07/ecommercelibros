package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Optional<Usuario> buscarPorIdUsuario(String idUsuario);

    Optional<Usuario> buscarPorCorreo(String correo);

    List<Usuario> listarTodos();

    void guardarUsuario(Usuario usuario);
}