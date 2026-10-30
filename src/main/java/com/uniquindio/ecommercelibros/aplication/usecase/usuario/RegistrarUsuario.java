package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Service
@Transactional
public class RegistrarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final VerificadorDeUsuario verificadorDeUsuario;
    private final PasswordEncoder passwordEncoder;

    public RegistrarUsuario(UsuarioRepository usuarioRepository,
                            VerificadorDeUsuario verificadorDeUsuario,
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.verificadorDeUsuario = verificadorDeUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(String nombre, Correo correo, String contraseniaPlana,
                             Telefono telefono, Direccion direccion) {
        return registrarConRol(nombre, correo, contraseniaPlana, telefono, direccion, RolUsuario.CLIENTE);
    }

    public Usuario registrarConRol(String nombre, Correo correo, String contraseniaPlana,
                                   Telefono telefono, Direccion direccion, RolUsuario rol) {
        Usuario.validarFormatoContrasenia(contraseniaPlana);
        verificadorDeUsuario.verificarQueCorreoNoExiste(correo.correo());

        String hash = passwordEncoder.encode(contraseniaPlana);
        Usuario usuario = Usuario.crear(UUID.randomUUID().toString(), nombre, correo, hash,
                telefono, direccion, rol);

        usuarioRepository.guardarUsuario(usuario);
        return usuario;
    }
}