package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrarUsuarioTest {

    private UsuarioRepository usuarioRepository;
    private VerificadorDeUsuario verificadorDeUsuario;
    private RegistrarUsuario registrarUsuario;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        verificadorDeUsuario = mock(VerificadorDeUsuario.class);
        registrarUsuario = new RegistrarUsuario(usuarioRepository, verificadorDeUsuario);
    }

    @Test
    void registrarUsuarioCorrectamente() {
        Usuario usuario = crearUsuario();

        Usuario resultado = registrarUsuario.registrar(usuario);

        assertSame(usuario, resultado);
        assertEquals(EstadoUsuario.ACTIVO, resultado.getEstadoUsuario());
        verify(verificadorDeUsuario).verificarQueCorreoNoExiste("juan@mail.com");
        verify(usuarioRepository, times(1)).guardarUsuario(usuario);
    }

    @Test
    void noDebeRegistrarUsuarioSiElCorreoYaExiste() {
        Usuario usuario = crearUsuario();

        doThrow(new ReglaDominioException("Ya existe un usuario registrado con ese correo"))
                .when(verificadorDeUsuario)
                .verificarQueCorreoNoExiste("juan@mail.com");

        assertThrows(ReglaDominioException.class,
                () -> registrarUsuario.registrar(usuario));

        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    private Usuario crearUsuario() {
        return Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "Clave123",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }
}