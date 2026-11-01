package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.EditorialPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({EditorialRepositoryJpa.class, EditorialPersistenceMapper.class})
class EditorialRepositoryJpaTest {

    @Autowired private EditorialRepository repository;
    @Autowired private EntityManager entityManager;

    @Test
    void guardarEditorial_seRecuperaConElMismoIdYDatos() {
        
        Editorial editorial = Editorial.crear("Editorial Persistida", Pais.ARGENTINA, "Descripcion");

       
        repository.guardarEditorial(editorial);
        sincronizar();
        Editorial recuperada = repository.buscarPorId(editorial.getIdEditorial()).orElseThrow();

    
        assertEquals(editorial.getIdEditorial(), recuperada.getIdEditorial());
        assertEquals("Editorial Persistida", recuperada.getNombreEditorial());
        assertEquals(Pais.ARGENTINA, recuperada.getPaisOrigen());
        assertEquals("Descripcion", recuperada.getDescripcionEditorial());
    }

    @Test
    void existePorNombre_conOtrasMayusculas_devuelveTrue() {
        
        repository.guardarEditorial(Editorial.crear("Sello Unico Prueba", Pais.MEXICO, null));
        sincronizar();

        
        assertTrue(repository.existePorNombre("sello unico PRUEBA"));
    }

    @Test
    void buscarPorId_idInexistente_devuelveVacio() {
        
        assertFalse(repository.buscarPorId(UUID.randomUUID()).isPresent());
    }

    @Test
    void listarTodas_incluyeLaEditorialGuardada() {
        
        Editorial editorial = Editorial.crear("Editorial Listada", Pais.CHILE, null);
        repository.guardarEditorial(editorial);
        sincronizar();

        
        assertTrue(repository.listarTodas().contains(editorial));
    }

    private void sincronizar() {
        entityManager.flush();
        entityManager.clear();
    }
}