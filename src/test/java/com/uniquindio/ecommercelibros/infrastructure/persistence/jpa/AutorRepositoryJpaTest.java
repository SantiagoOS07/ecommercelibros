package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.AutorEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.AutorPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({AutorRepositoryJpa.class, AutorPersistenceMapper.class})
class AutorRepositoryJpaTest {

    @Autowired private AutorRepository repository;
    @Autowired private EntityManager entityManager;

    @Test
    void guardarAutor_conVariasNacionalidades_seRecuperaIgual() {
        
        Autor autor = crearAutor(idUnico(), "Autora Persistida");
        autor.agregarNacionalidad(Nacionalidad.ESPANOLA);

        
        repository.guardarAutor(autor);
        sincronizar();
        Autor recuperado = repository.buscarPorIdAutor(autor.getIdAutor()).orElseThrow();

        
        assertEquals("Autora Persistida", recuperado.getNombreAutor());
        assertEquals("Biografia de prueba", recuperado.getBiografia());
        assertEquals(Set.of(Nacionalidad.COLOMBIANA, Nacionalidad.ESPANOLA), Set.copyOf(recuperado.getNacionalidades()));
        assertFalse(recuperado.isEliminado());
    }

    @Test
    void buscarPorIdAutor_autorEliminado_noLoDevuelvePeroLaFilaSigueExistiendo() {
        
        Autor autor = crearAutor(idUnico(), "Autor Por Eliminar");
        repository.guardarAutor(autor);
        autor.eliminarLogicamente();
        repository.guardarAutor(autor);
        sincronizar();

        
        boolean encontrado = repository.buscarPorIdAutor(autor.getIdAutor()).isPresent();

        
        assertFalse(encontrado);
        AutorEntity fila = entityManager.find(AutorEntity.class, autor.getIdAutor());
        assertNotNull(fila);
        assertTrue(fila.isEliminado());
        assertTrue(repository.existePorId(autor.getIdAutor()));
    }

    @Test
    void listarTodos_conUnAutorEliminado_soloDevuelveLosActivos() {
        
        Autor activo = crearAutor(idUnico(), "Autor Activo Listado");
        Autor eliminado = crearAutor(idUnico(), "Autor Eliminado Listado");
        eliminado.eliminarLogicamente();
        repository.guardarAutor(activo);
        repository.guardarAutor(eliminado);
        sincronizar();

        
        List<String> ids = repository.listarTodos().stream().map(Autor::getIdAutor).toList();

        
        assertTrue(ids.contains(activo.getIdAutor()));
        assertFalse(ids.contains(eliminado.getIdAutor()));
    }

    @Test
    void buscarPorNombre_conOtrasMayusculas_loEncuentra() {
        
        Autor autor = crearAutor(idUnico(), "Rosa Montero Unica");
        repository.guardarAutor(autor);
        sincronizar();

        
        boolean encontrado = repository.buscarPorNombre("rosa montero UNICA").isPresent();

        
        assertTrue(encontrado);
    }

    @Test
    void buscarPorNombre_autorEliminado_noLoDevuelve() {
        
        Autor autor = crearAutor(idUnico(), "Nombre De Autor Borrado");
        autor.eliminarLogicamente();
        repository.guardarAutor(autor);
        sincronizar();

        
        boolean encontrado = repository.buscarPorNombre("Nombre De Autor Borrado").isPresent();

        
        assertFalse(encontrado);
    }

    @Test
    void existePorId_idNuncaUsado_devuelveFalse() {
        
        assertFalse(repository.existePorId(idUnico()));
    }

    private void sincronizar() {
        entityManager.flush();
        entityManager.clear();
    }

    private static String idUnico() {
        return UUID.randomUUID().toString();
    }

    private static Autor crearAutor(String id, String nombre) {
        return Autor.crear(id, nombre, "Biografia de prueba", Nacionalidad.COLOMBIANA);
    }
}