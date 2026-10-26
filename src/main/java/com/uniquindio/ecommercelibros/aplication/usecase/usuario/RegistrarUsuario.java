package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final VerificadorDeUsuario verificadorDeUsuario;

    public RegistrarUsuario(UsuarioRepository usuarioRepository, VerificadorDeUsuario verificadorDeUsuario) {
        this.usuarioRepository = usuarioRepository;
        this.verificadorDeUsuario = verificadorDeUsuario;
    }

    public Usuario registrar(Usuario usuario) {
        verificadorDeUsuario.verificarQueCorreoNoExiste(usuario.getCorreo().correo());
        usuarioRepository.guardarUsuario(usuario);
        return usuario;
    }
}