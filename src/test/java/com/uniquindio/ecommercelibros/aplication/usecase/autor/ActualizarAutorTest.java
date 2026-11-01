package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActualizarAutorTest {

    private AutorRepository autorRepository;
    private ActualizarAutor actualizarAutor;
    private Autor autor;

    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
        actualizarAutor = new ActualizarAutor(autorRepository);
        autor = Autor.crear("autor-1", "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA);
    }

    @Test
    void actualizarNombre_autorExistente_cambiaElNombreYGuarda() {
      
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));

    
        actualizarAutor.actualizarNombre("autor-1", "Gabo");

        
        assertEquals("Gabo", autor.getNombreAutor());
        verify(autorRepository).guardarAutor(autor);
    }

    @Test
    void actualizarNombre_nombreVacio_lanzaReglaDominioYNuncaGuarda() {
      
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));

       
        assertThrows(ReglaDominioException.class, () -> actualizarAutor.actualizarNombre("autor-1", " "));
        verify(autorRepository, never()).guardarAutor(any());
    }

    @Test
    void actualizarBiografia_autorInexistente_lanzaRecursoNoEncontradoYNuncaGuarda() {
      
        when(autorRepository.buscarPorIdAutor("no-existe")).thenReturn(Optional.empty());

       
        assertThrows(RecursoNoEncontradoException.class,
                () -> actualizarAutor.actualizarBiografia("no-existe", "Bio"));
        verify(autorRepository, never()).guardarAutor(any());
    }

    @Test
    void agregarNacionalidad_nueva_laAgregaYGuarda() {
      
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));

    
        actualizarAutor.agregarNacionalidad("autor-1", Nacionalidad.MEXICANA);

        
        assertTrue(autor.getNacionalidades().contains(Nacionalidad.MEXICANA));
        verify(autorRepository).guardarAutor(autor);
    }

    @Test
    void eliminarNacionalidad_laUnica_lanzaReglaDominioYNuncaGuarda() {
      
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));

       
        assertThrows(ReglaDominioException.class,
                () -> actualizarAutor.eliminarNacionalidad("autor-1", Nacionalidad.COLOMBIANA));
        verify(autorRepository, never()).guardarAutor(any());
    }
}