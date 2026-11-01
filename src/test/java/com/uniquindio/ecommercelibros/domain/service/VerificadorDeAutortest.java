package com.uniquindio.ecommercelibros.domain.service;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VerificadorDeAutorTest {

    private AutorRepository autorRepository;
    private LibroRepository libroRepository;
    private VerificadorDeAutor verificador;

    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
        libroRepository = mock(LibroRepository.class);
        verificador = new VerificadorDeAutor(autorRepository, libroRepository);
    }

    @Test
    void verificarQueAutorNoExiste_idOcupado_lanzaRecursoDuplicado() {
        
        when(autorRepository.existePorId("autor-1")).thenReturn(true);

        
        assertThrows(RecursoDuplicadoException.class, () -> verificador.verificarQueAutorNoExiste("autor-1"));
    }

    @Test
    void verificarQueAutorNoExiste_idLibre_noLanzaExcepcion() {
        
        when(autorRepository.existePorId("autor-1")).thenReturn(false);

        
        assertDoesNotThrow(() -> verificador.verificarQueAutorNoExiste("autor-1"));
    }

    @Test
    void verificarQueAutorExiste_autorInexistente_lanzaRecursoNoEncontrado() {
        
        when(autorRepository.buscarPorIdAutor("no-existe")).thenReturn(Optional.empty());

        
        assertThrows(RecursoNoEncontradoException.class, () -> verificador.verificarQueAutorExiste("no-existe"));
    }

    @Test
    void verificarQueAutorExiste_autorRegistrado_noLanzaExcepcion() {
        
        when(autorRepository.buscarPorIdAutor("autor-1"))
                .thenReturn(Optional.of(Autor.crear("autor-1", "Nombre", null, Nacionalidad.COLOMBIANA)));

        
        assertDoesNotThrow(() -> verificador.verificarQueAutorExiste("autor-1"));
    }

    @Test
    void verificarQueNoTieneLibros_conLibros_lanzaReglaDominio() {
        
        when(libroRepository.buscarPorAutor("autor-1")).thenReturn(List.of(mock(Libro.class)));

        
        assertThrows(ReglaDominioException.class, () -> verificador.verificarQueNoTieneLibros("autor-1"));
    }

    @Test
    void verificarQueNoTieneLibros_sinLibros_noLanzaExcepcion() {
        
        when(libroRepository.buscarPorAutor("autor-1")).thenReturn(List.of());

        
        assertDoesNotThrow(() -> verificador.verificarQueNoTieneLibros("autor-1"));
    }
}