package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultarUsuarioTest {

    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
    }

    @Test
    void consultarUsuarioPorIdExistente() {
        Usuario usuario = crearUsuario("user-1", "juan@mail.com");
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = new ConsultarUsuarioID(usuarioRepository).consultar("user-1");

        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get());
    }

    @Test
    void consultarUsuarioPorIdInexistenteRetornaVacio() {
        when(usuarioRepository.buscarPorIdUsuario("no-existe")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = new ConsultarUsuarioID(usuarioRepository).consultar("no-existe");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarUsuarioPorCorreo() {
        Usuario usuario = crearUsuario("user-1", "juan@mail.com");
        when(usuarioRepository.buscarPorCorreo("juan@mail.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = new ConsultarUsuarioCorreo(usuarioRepository).buscar("juan@mail.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan@mail.com", resultado.get().getCorreo().correo());
    }

    @Test
    void listarTodosLosUsuarios() {
        List<Usuario> usuarios = List.of(
                crearUsuario("user-1", "juan@mail.com"),
                crearUsuario("user-2", "ana@mail.com"));
        when(usuarioRepository.listarTodos()).thenReturn(usuarios);

        List<Usuario> resultado = new ListarUsuarios(usuarioRepository).listar();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).listarTodos();
    }

    private Usuario crearUsuario(String id, String correo) {
        return Usuario.crear(id, "Juan Perez", new Correo(correo), "Clave123",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }
}