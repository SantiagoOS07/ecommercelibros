package com.uniquindio.ecommercelibros.aplication.usecase.editorial;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsultarEditorialTest {

    private EditorialRepository editorialRepository;

    @BeforeEach
    void setUp() {
        editorialRepository = mock(EditorialRepository.class);
    }

    @Test
    void consultar_editorialExistente_laDevuelve() {
        
        Editorial editorial = Editorial.crear("Planeta", Pais.ESPANA, null);
        when(editorialRepository.buscarPorId(editorial.getIdEditorial())).thenReturn(Optional.of(editorial));

               Editorial resultado = new ConsultarEditorialID(editorialRepository).consultar(editorial.getIdEditorial());

        
        assertEquals(editorial, resultado);
    }

    @Test
    void consultar_editorialInexistente_lanzaRecursoNoEncontrado() {
        
        UUID id = UUID.randomUUID();
        when(editorialRepository.buscarPorId(id)).thenReturn(Optional.empty());

      
        assertThrows(RecursoNoEncontradoException.class,
                () -> new ConsultarEditorialID(editorialRepository).consultar(id));
    }

    @Test
    void listar_conEditoriales_devuelveLasDelRepositorio() {
        
        when(editorialRepository.listarTodas()).thenReturn(List.of(
                Editorial.crear("Planeta", Pais.ESPANA, null),
                Editorial.crear("Norma", Pais.COLOMBIA, null)));

               List<Editorial> resultado = new ListarEditoriales(editorialRepository).listar();

        
        assertEquals(2, resultado.size());
    }
}