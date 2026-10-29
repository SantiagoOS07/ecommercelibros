package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.EncriptadorContrasenia;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrarUsuarioTest {

    private UsuarioRepository usuarioRepository;
    private VerificadorDeUsuario verificadorDeUsuario;
    private EncriptadorContrasenia encriptador;
    private RegistrarUsuario registrarUsuario;

    private final Correo correo = new Correo("juan@mail.com");
    private final Telefono telefono = new Telefono("3001234567");
    private final Direccion direccion = new Direccion("Calle 1");

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        verificadorDeUsuario = mock(VerificadorDeUsuario.class);
        encriptador = mock(EncriptadorContrasenia.class);
        registrarUsuario = new RegistrarUsuario(usuarioRepository, verificadorDeUsuario, encriptador);
    }

    @Test
    void registrarUsuarioCorrectamenteComoClienteYGuardaElHash() {
        when(encriptador.encriptar("Clave123")).thenReturn("{bcrypt}hash");

        Usuario resultado = registrarUsuario.registrar("Juan Perez", correo, "Clave123", telefono, direccion);

        assertEquals(RolUsuario.CLIENTE, resultado.getRolUsuario());
        assertEquals(EstadoUsuario.ACTIVO, resultado.getEstadoUsuario());
        assertEquals("{bcrypt}hash", resultado.getContrasenia());
        assertNotEquals("Clave123", resultado.getContrasenia());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).guardarUsuario(captor.capture());
        assertEquals("{bcrypt}hash", captor.getValue().getContrasenia());
        verify(verificadorDeUsuario).verificarQueCorreoNoExiste("juan@mail.com");
    }

    @Test
    void registrarConRolPermiteCrearUnAdministrador() {
        when(encriptador.encriptar("Clave123")).thenReturn("{bcrypt}hash");

        Usuario resultado = registrarUsuario.registrarConRol(
                "Admin", correo, "Clave123", telefono, direccion, RolUsuario.ADMINISTRADOR);

        assertEquals(RolUsuario.ADMINISTRADOR, resultado.getRolUsuario());
    }

    @Test
    void noDebeRegistrarUsuarioSiElCorreoYaExiste() {
        doThrow(new ReglaDominioException("Ya existe un usuario registrado con ese correo"))
                .when(verificadorDeUsuario)
                .verificarQueCorreoNoExiste("juan@mail.com");

        assertThrows(ReglaDominioException.class,
                () -> registrarUsuario.registrar("Juan Perez", correo, "Clave123", telefono, direccion));

        verify(encriptador, never()).encriptar(any());
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    @Test
    void noDebeRegistrarConContraseniaInseguraNiHashearla() {
        assertThrows(ReglaDominioException.class,
                () -> registrarUsuario.registrar("Juan Perez", correo, "clavesegura", telefono, direccion));

        verify(encriptador, never()).encriptar(any());
        verify(usuarioRepository, never()).guardarUsuario(any());
    }
}