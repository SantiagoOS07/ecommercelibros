package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActualizarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final VerificadorDeUsuario verificadorDeUsuario;

    public ActualizarUsuario(UsuarioRepository usuarioRepository, VerificadorDeUsuario verificadorDeUsuario) {
        this.usuarioRepository = usuarioRepository;
        this.verificadorDeUsuario = verificadorDeUsuario;
    }

    public void actualizarNombre(String idUsuario, String nuevoNombre) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.cambiarNombre(nuevoNombre);
        usuarioRepository.guardarUsuario(usuario);
    }

    public void actualizarCorreo(String idUsuario, Correo nuevoCorreo) {
        Usuario usuario = obtenerUsuario(idUsuario);

        if (!usuario.getCorreo().equals(nuevoCorreo)) {
            verificadorDeUsuario.verificarQueCorreoNoExiste(nuevoCorreo.correo());
        }

        usuario.cambiarCorreo(nuevoCorreo);
        usuarioRepository.guardarUsuario(usuario);
    }

    public void actualizarTelefono(String idUsuario, Telefono nuevoTelefono) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.cambiarTelefono(nuevoTelefono);
        usuarioRepository.guardarUsuario(usuario);
    }

    public void actualizarDireccion(String idUsuario, Direccion nuevaDireccion) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.cambiarDireccion(nuevaDireccion);
        usuarioRepository.guardarUsuario(usuario);
    }

    public void actualizarRol(String idUsuario, RolUsuario nuevoRol) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.cambiarRol(nuevoRol);
        usuarioRepository.guardarUsuario(usuario);
    }

    public void cambiarContrasenia(String idUsuario, String contraseniaActual, String nuevaContrasenia) {
        Usuario usuario = obtenerUsuario(idUsuario);

        if (!usuario.coincideContrasenia(contraseniaActual)) {
            throw new ReglaDominioException("La contraseña actual no es correcta.");
        }

        usuario.cambiarContrasenia(nuevaContrasenia);
        usuarioRepository.guardarUsuario(usuario);
    }

    public void reactivar(String idUsuario) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.activar();
        usuarioRepository.guardarUsuario(usuario);
    }

    private Usuario obtenerUsuario(String idUsuario) {
        return usuarioRepository.buscarPorIdUsuario(idUsuario)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El usuario no existe"));
    }
}