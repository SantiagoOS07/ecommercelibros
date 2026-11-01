package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeAutor;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EliminarAutorTest {

    private AutorRepository autorRepository;
    private VerificadorDeAutor verificadorDeAutor;
    private EliminarAutor eliminarAutor;

    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
        verificadorDeAutor = mock(VerificadorDeAutor.class);
        eliminarAutor = new EliminarAutor(autorRepository, verificadorDeAutor);
    }

    @Test
    void eliminar_autorSinLibros_loMarcaComoEliminadoYLoGuarda() {
        
        Autor autor = crearAutor();
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));

       
        eliminarAutor.eliminar("autor-1");

        
        ArgumentCaptor<Autor> captor = ArgumentCaptor.forClass(Autor.class);
        verify(autorRepository).guardarAutor(captor.capture());
        assertTrue(captor.getValue().isEliminado());
    }

    @Test
    void eliminar_autorConLibrosNoEliminados_lanzaReglaDominioYNuncaGuarda() {
        
        Autor autor = crearAutor();
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));
        doThrow(new ReglaDominioException("No se puede eliminar un autor que tiene libros registrados"))
                .when(verificadorDeAutor).verificarQueNoTieneLibros("autor-1");

       
        assertThrows(ReglaDominioException.class, () -> eliminarAutor.eliminar("autor-1"));

        
        assertFalse(autor.isEliminado());
        verify(autorRepository, never()).guardarAutor(any());
    }

    @Test
    void eliminar_autorInexistenteOYaEliminado_lanzaRecursoNoEncontradoYNuncaGuarda() {
        
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.empty());

       
        assertThrows(RecursoNoEncontradoException.class, () -> eliminarAutor.eliminar("autor-1"));

        
        verify(verificadorDeAutor, never()).verificarQueNoTieneLibros(any());
        verify(autorRepository, never()).guardarAutor(any());
    }

    private Autor crearAutor() {
        return Autor.crear("autor-1", "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA);
    }
}