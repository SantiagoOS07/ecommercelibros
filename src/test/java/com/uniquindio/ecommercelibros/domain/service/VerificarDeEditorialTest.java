package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VerificadorDeEditorialTest {

    private EditorialRepository editorialRepository;
    private VerificadorDeEditorial verificador;

    @BeforeEach
    void setUp() {
        editorialRepository = mock(EditorialRepository.class);
        verificador = new VerificadorDeEditorial(editorialRepository);
    }

    @Test
    void verificarQueNombreNoExiste_nombreRegistrado_lanzaRecursoDuplicado() {
        
        when(editorialRepository.existePorNombre("Planeta")).thenReturn(true);

        
        assertThrows(RecursoDuplicadoException.class, () -> verificador.verificarQueNombreNoExiste("Planeta"));
    }

    @Test
    void verificarQueEditorialExiste_inexistente_lanzaRecursoNoEncontrado() {
        
        UUID id = UUID.randomUUID();
        when(editorialRepository.buscarPorId(id)).thenReturn(Optional.empty());

        
        assertThrows(RecursoNoEncontradoException.class, () -> verificador.verificarQueEditorialExiste(id));
    }

    @Test
    void verificarQueEditorialExiste_registrada_noLanzaExcepcion() {
        
        Editorial editorial = Editorial.crear("Planeta", Pais.COLOMBIA, null);
        when(editorialRepository.buscarPorId(editorial.getIdEditorial())).thenReturn(Optional.of(editorial));

        
        assertDoesNotThrow(() -> verificador.verificarQueEditorialExiste(editorial.getIdEditorial()));
    }
}