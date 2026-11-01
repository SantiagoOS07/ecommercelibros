package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.UsuarioPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({UsuarioRepositoryJpa.class, UsuarioPersistenceMapper.class})
class UsuarioRepositoryJpaTest {

    @Autowired private UsuarioRepository repository;
    @Autowired private EntityManager entityManager;

    @Test
    void guardarUsuario_conSusValueObjects_seRecuperaIgual() {
        
        Usuario usuario = crearUsuario("persistido@mail.com");

        
        repository.guardarUsuario(usuario);
        sincronizar();
        Usuario recuperado = repository.buscarPorIdUsuario(usuario.getIdUsuario()).orElseThrow();

        
        assertEquals("Juan Perez", recuperado.getNombre());
        assertEquals(new Correo("persistido@mail.com"), recuperado.getCorreo());
        assertEquals("{bcrypt}hash", recuperado.getContrasenia());
        assertEquals(new Telefono("3001234567"), recuperado.getTelefono());
        assertEquals(new Direccion("Calle 1"), recuperado.getDireccion());
        assertEquals(RolUsuario.CLIENTE, recuperado.getRolUsuario());
        assertEquals(EstadoUsuario.ACTIVO, recuperado.getEstadoUsuario());
    }

    @Test
    void buscarPorCorreo_conOtrasMayusculas_loEncuentra() {
        
        Usuario usuario = crearUsuario("mayusculas@mail.com");
        repository.guardarUsuario(usuario);
        sincronizar();

        
        boolean encontrado = repository.buscarPorCorreo("MAYUSCULAS@Mail.com").isPresent();

        
        assertTrue(encontrado);
    }

    @Test
    void guardarUsuario_desactivado_conservaLaFilaComoInactivo() {
        
        Usuario usuario = crearUsuario("desactivado@mail.com");
        repository.guardarUsuario(usuario);
        usuario.desactivar();

        
        repository.guardarUsuario(usuario);
        sincronizar();

        
        Usuario recuperado = repository.buscarPorIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(EstadoUsuario.INACTIVO, recuperado.getEstadoUsuario());
    }

    @Test
    void listarTodos_conUsuariosGuardados_losIncluye() {
        
        Usuario uno = crearUsuario("lista1@mail.com");
        Usuario dos = crearUsuario("lista2@mail.com");
        repository.guardarUsuario(uno);
        repository.guardarUsuario(dos);
        sincronizar();

        
        List<String> ids = repository.listarTodos().stream().map(Usuario::getIdUsuario).toList();

        
        assertTrue(ids.containsAll(List.of(uno.getIdUsuario(), dos.getIdUsuario())));
    }

    private void sincronizar() {
        entityManager.flush();
        entityManager.clear();
    }

    private static Usuario crearUsuario(String correo) {
        return Usuario.crear(UUID.randomUUID().toString(), "Juan Perez", new Correo(correo), "{bcrypt}hash",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }
}