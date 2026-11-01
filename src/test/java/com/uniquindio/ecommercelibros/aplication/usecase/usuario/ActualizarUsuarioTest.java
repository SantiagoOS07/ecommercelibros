package com.uniquindio.ecommercelibros.aplication.usecase.usuario;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActualizarUsuarioTest {

    private static final String HASH_ACTUAL = "{bcrypt}hash-actual";

    private UsuarioRepository usuarioRepository;
    private VerificadorDeUsuario verificadorDeUsuario;
    private PasswordEncoder passwordEncoder;
    private ActualizarUsuario actualizarUsuario;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        verificadorDeUsuario = mock(VerificadorDeUsuario.class);
        passwordEncoder = mock(PasswordEncoder.class);
        actualizarUsuario = new ActualizarUsuario(usuarioRepository, verificadorDeUsuario, passwordEncoder);

        usuario = Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), HASH_ACTUAL,
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }

   

    @Test
    void actualizarNombre_usuarioActivo_cambiaElNombreYGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.actualizarNombre("user-1", "Juan Carlos");

        
        assertEquals("Juan Carlos", usuario.getNombre());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void actualizarNombre_usuarioInexistente_lanzaRecursoNoEncontradoYNuncaGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("no-existe")).thenReturn(Optional.empty());

     
        assertThrows(RecursoNoEncontradoException.class,
                () -> actualizarUsuario.actualizarNombre("no-existe", "Juan Carlos"));
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    @Test
    void actualizarNombre_usuarioInactivo_lanzaReglaDominioYNuncaGuarda() {
      
        usuario.desactivar();
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        assertThrows(ReglaDominioException.class, () -> actualizarUsuario.actualizarNombre("user-1", "Otro"));

        
        assertEquals("Juan Perez", usuario.getNombre());
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    @Test
    void actualizarTelefono_usuarioActivo_cambiaElTelefonoYGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.actualizarTelefono("user-1", new Telefono("3109876543"));

        
        assertEquals(new Telefono("3109876543"), usuario.getTelefono());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void actualizarDireccion_usuarioActivo_cambiaLaDireccionYGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.actualizarDireccion("user-1", new Direccion("Carrera 20"));

        
        assertEquals(new Direccion("Carrera 20"), usuario.getDireccion());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void actualizarRol_usuarioActivo_cambiaElRolYGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.actualizarRol("user-1", RolUsuario.PROVEEDOR);

        
        assertEquals(RolUsuario.PROVEEDOR, usuario.getRolUsuario());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    // --- Correo ---

    @Test
    void actualizarCorreo_correoNuevoLibre_verificaDuplicadoYGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.actualizarCorreo("user-1", new Correo("nuevo@mail.com"));

        
        verify(verificadorDeUsuario).verificarQueCorreoNoExiste("nuevo@mail.com");
        assertEquals(new Correo("nuevo@mail.com"), usuario.getCorreo());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void actualizarCorreo_mismoCorreoConOtrasMayusculas_noLoTrataComoDuplicado() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.actualizarCorreo("user-1", new Correo("JUAN@mail.com"));

        
        verify(verificadorDeUsuario, never()).verificarQueCorreoNoExiste(anyString());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void actualizarCorreo_correoDeOtroUsuario_lanzaRecursoDuplicadoYNuncaGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));
        doThrow(new RecursoDuplicadoException("Ya existe un usuario registrado con ese correo"))
                .when(verificadorDeUsuario).verificarQueCorreoNoExiste("ana@mail.com");

      
        assertThrows(RecursoDuplicadoException.class,
                () -> actualizarUsuario.actualizarCorreo("user-1", new Correo("ana@mail.com")));

        
        assertEquals(new Correo("juan@mail.com"), usuario.getCorreo());
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    

    @Test
    void cambiarContrasenia_datosCorrectos_guardaElNuevoHash() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123", HASH_ACTUAL)).thenReturn(true);
        when(passwordEncoder.matches("NuevaClave456", HASH_ACTUAL)).thenReturn(false);
        when(passwordEncoder.encode("NuevaClave456")).thenReturn("{bcrypt}hash-nuevo");

      
        actualizarUsuario.cambiarContrasenia("user-1", "Clave123", "NuevaClave456");

        
        assertEquals("{bcrypt}hash-nuevo", usuario.getContrasenia());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void cambiarContrasenia_actualIncorrecta_lanzaReglaDominioYNuncaGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("OtraClave1", HASH_ACTUAL)).thenReturn(false);

      
        assertThrows(ReglaDominioException.class,
                () -> actualizarUsuario.cambiarContrasenia("user-1", "OtraClave1", "NuevaClave456"));

        
        assertEquals(HASH_ACTUAL, usuario.getContrasenia());
        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    @Test
    void cambiarContrasenia_nuevaInsegura_lanzaReglaDominioYNuncaGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123", HASH_ACTUAL)).thenReturn(true);

     
        assertThrows(ReglaDominioException.class,
                () -> actualizarUsuario.cambiarContrasenia("user-1", "Clave123", "sinmayuscula1"));
        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    @Test
    void cambiarContrasenia_nuevaIgualALaActual_lanzaReglaDominioYNuncaGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123", HASH_ACTUAL)).thenReturn(true);

     
        assertThrows(ReglaDominioException.class,
                () -> actualizarUsuario.cambiarContrasenia("user-1", "Clave123", "Clave123"));
        verify(usuarioRepository, never()).guardarUsuario(any());
    }

    

    @Test
    void reactivar_usuarioInactivo_loDejaActivoYGuarda() {
      
        usuario.desactivar();
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

      
        actualizarUsuario.reactivar("user-1");

        
        assertEquals(EstadoUsuario.ACTIVO, usuario.getEstadoUsuario());
        verify(usuarioRepository).guardarUsuario(usuario);
    }

    @Test
    void reactivar_usuarioYaActivo_lanzaReglaDominioYNuncaGuarda() {
      
        when(usuarioRepository.buscarPorIdUsuario("user-1")).thenReturn(Optional.of(usuario));

     
        assertThrows(ReglaDominioException.class, () -> actualizarUsuario.reactivar("user-1"));
        verify(usuarioRepository, never()).guardarUsuario(any());
    }
}