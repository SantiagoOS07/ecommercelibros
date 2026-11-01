package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VerificadorDeUsuarioTest {

    private UsuarioRepository usuarioRepository;
    private VerificadorDeUsuario verificador;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        verificador = new VerificadorDeUsuario(usuarioRepository);
    }

    @Test
    void verificarQueCorreoNoExiste_correoRegistrado_lanzaRecursoDuplicado() {
       
        when(usuarioRepository.buscarPorCorreo("juan@mail.com")).thenReturn(Optional.of(usuario()));

        
        assertThrows(RecursoDuplicadoException.class, () -> verificador.verificarQueCorreoNoExiste("juan@mail.com"));
    }

    @Test
    void verificarQueCorreoNoExiste_correoLibre_noLanzaExcepcion() {
       
        when(usuarioRepository.buscarPorCorreo("nuevo@mail.com")).thenReturn(Optional.empty());

        
        assertDoesNotThrow(() -> verificador.verificarQueCorreoNoExiste("nuevo@mail.com"));
    }

    @Test
    void verificarQueUsuarioExisteYEstaActivo_usuarioActivo_noLanzaExcepcion() {
       
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario()));

        
        assertDoesNotThrow(() -> verificador.verificarQueUsuarioExisteYEstaActivo("user-1"));
    }

    @Test
    void verificarQueUsuarioExisteYEstaActivo_usuarioInexistente_lanzaRecursoNoEncontrado() {
       
        when(usuarioRepository.buscarPorIdUsuario("no-existe")).thenReturn(Optional.empty());

        
        assertThrows(RecursoNoEncontradoException.class,
                () -> verificador.verificarQueUsuarioExisteYEstaActivo("no-existe"));
    }

    @Test
    void verificarQueUsuarioExisteYEstaActivo_usuarioInactivo_lanzaReglaDominio() {
       
        Usuario inactivo = usuario();
        inactivo.desactivar();
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(inactivo));

        
        assertThrows(ReglaDominioException.class,
                () -> verificador.verificarQueUsuarioExisteYEstaActivo("user-1"));
    }

    private Usuario usuario() {
        return Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "{bcrypt}hash",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }
}