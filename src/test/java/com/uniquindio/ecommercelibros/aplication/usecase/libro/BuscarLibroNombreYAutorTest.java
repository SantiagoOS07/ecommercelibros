package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscarLibroNombreYAutorTest {

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private BuscarLibroAutor buscarLibroAutor;
    private final Autor autor = Autor.crear("autor-1", "Camila Restrepo", null, Nacionalidad.COLOMBIANA);

    @BeforeEach
    void setUp() {
        libroRepository = mock(LibroRepository.class);
        autorRepository = mock(AutorRepository.class);
        buscarLibroAutor = new BuscarLibroAutor(libroRepository, autorRepository);
    }

    @Test
    void buscarPorNombre_conCoincidencias_devuelveLosLibrosDelRepositorio() {
        
        List<Libro> libros = List.of(mock(Libro.class), mock(Libro.class));
        when(libroRepository.buscarPorNombre("rio")).thenReturn(libros);

     
        List<Libro> resultado = new BuscarLibroNombre(libroRepository).buscar("rio");

        
        assertEquals(libros, resultado);
    }

    @Test
    void buscarPorNombreDeAutor_autorExistente_devuelveSusLibros() {
        
        List<Libro> libros = List.of(mock(Libro.class));
        when(autorRepository.buscarPorNombre("Camila Restrepo")).thenReturn(Optional.of(autor));
        when(libroRepository.buscarPorAutor("autor-1")).thenReturn(libros);

     
        List<Libro> resultado = buscarLibroAutor.buscar("Camila Restrepo");

        
        assertEquals(libros, resultado);
    }

    @Test
    void buscarPorNombreDeAutor_autorInexistente_devuelveListaVaciaSinConsultarLibros() {
        
        when(autorRepository.buscarPorNombre("Nadie")).thenReturn(Optional.empty());

     
        List<Libro> resultado = buscarLibroAutor.buscar("Nadie");

        
        assertTrue(resultado.isEmpty());
        verify(libroRepository, never()).buscarPorAutor(anyString());
    }

    @Test
    void buscarPorIdAutor_autorExistente_devuelveSusLibros() {
        
        List<Libro> libros = List.of(mock(Libro.class));
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));
        when(libroRepository.buscarPorAutor("autor-1")).thenReturn(libros);

     
        List<Libro> resultado = buscarLibroAutor.buscarPorIdAutor("autor-1");

        
        assertEquals(libros, resultado);
    }

    @Test
    void buscarPorIdAutor_autorInexistenteOEliminado_lanzaRecursoNoEncontradoSinConsultarLibros() {
        
        when(autorRepository.buscarPorIdAutor("no-existe")).thenReturn(Optional.empty());

        
        assertThrows(RecursoNoEncontradoException.class, () -> buscarLibroAutor.buscarPorIdAutor("no-existe"));
        verify(libroRepository, never()).buscarPorAutor(anyString());
    }
}