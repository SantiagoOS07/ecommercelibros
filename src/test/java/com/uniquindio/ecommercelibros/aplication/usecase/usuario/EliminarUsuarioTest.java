package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EliminarUsuarioTest {

    private UsuarioRepository usuarioRepository;
    private EliminarUsuario eliminarUsuario;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        eliminarUsuario = new EliminarUsuario(usuarioRepository);
    }

    @Test
    void eliminarDesactivaAlUsuarioSinBorrarlo() {
        Usuario usuario = crearUsuario();
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

        eliminarUsuario.eliminar("user-1");

        assertEquals(EstadoUsuario.INACTIVO, usuario.getEstadoUsuario());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void noDebeEliminarUsuarioQueYaEstaInactivo() {
        Usuario usuario = crearUsuario();
        usuario.desactivar();
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

        assertThrows(ReglaDominioException.class,
                () -> eliminarUsuario.eliminar("user-1"));

        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    @Test
    void noDebeEliminarUsuarioQueNoExiste() {
        when(usuarioRepository.buscarPorIdUsuario("no-existe")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> eliminarUsuario.eliminar("no-existe"));

        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    private Usuario crearUsuario() {
        return Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "Clave123",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }
}