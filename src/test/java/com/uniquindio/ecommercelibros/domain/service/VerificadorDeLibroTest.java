package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificadorDeLibroTest {

    @Mock private LibroRepository libroRepository;

    @Test
    void verificarQueISBNNoExiste_conIsbnRepetido_lanzaDuplicado() { // [M-19]
        when(libroRepository.existePorIsbn("9783161484100")).thenReturn(true);

        RecursoDuplicadoException ex = assertThrows(RecursoDuplicadoException.class,
                () -> new VerificadorDeLibro(libroRepository).verificarQueISBNNoExiste("9783161484100"));

        assertEquals("Ya existe un libro registrado con ese ISBN", ex.getMessage());
    }

    @Test
    void verificarQueISBNNoExiste_conIsbnLibre_noLanza() {
        when(libroRepository.existePorIsbn("9783161484100")).thenReturn(false);

        assertDoesNotThrow(() -> new VerificadorDeLibro(libroRepository).verificarQueISBNNoExiste("9783161484100"));
    }
}