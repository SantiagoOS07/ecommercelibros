package com.uniquindio.ecommercelibros.infrastructure.security;

import com.uniquindio.ecommercelibros.aplication.usecase.usuario.RegistrarUsuario;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UsuariosIniciales implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RegistrarUsuario registrarUsuario;
    private final String correoAdmin;
    private final String contraseniaAdmin;

    public UsuariosIniciales(UsuarioRepository usuarioRepository,
                             RegistrarUsuario registrarUsuario,
                             @Value("${app.seed.admin.correo}") String correoAdmin,
                             @Value("${app.seed.admin.contrasenia}") String contraseniaAdmin) {
        this.usuarioRepository = usuarioRepository;
        this.registrarUsuario = registrarUsuario;
        this.correoAdmin = correoAdmin;
        this.contraseniaAdmin = contraseniaAdmin;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.buscarPorCorreo(correoAdmin).isPresent()) {
            return;
        }
        registrarUsuario.registrarConRol(
                "Administrador", new Correo(correoAdmin), contraseniaAdmin,
                new Telefono("3000000000"), new Direccion("Oficina principal"),
                RolUsuario.ADMINISTRADOR);
    }
}