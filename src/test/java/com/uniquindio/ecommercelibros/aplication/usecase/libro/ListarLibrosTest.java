package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.FiltroLibros;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListarLibrosTest {

    private LibroRepository libroRepository;
    private ListarLibros listarLibros;

    @BeforeEach
    void setUp() {
        libroRepository = mock(LibroRepository.class);
        listarLibros = new ListarLibros(libroRepository);
    }

    @Test
    void ejecutar_conFiltrosValidos_pasaTodosLosFiltrosAlRepositorio() {
      
        Pagina<Libro> esperada = new Pagina<>(List.of(), 0, 0, 1, 5);
        when(libroRepository.buscarLibros(any(FiltroLibros.class), eq(1), eq(5))).thenReturn(esperada);

       
        Pagina<Libro> resultado = listarLibros.ejecutar("rio", EstadoLibro.DISPONIBLE, CategoriaLibro.NOVELA,
                10.0, 50.0, Moneda.USD, 1, 5);

       
        assertSame(esperada, resultado);
        ArgumentCaptor<FiltroLibros> captor = ArgumentCaptor.forClass(FiltroLibros.class);
        verify(libroRepository).buscarLibros(captor.capture(), eq(1), eq(5));
        assertEquals(new FiltroLibros("rio", EstadoLibro.DISPONIBLE, CategoriaLibro.NOVELA, 10.0, 50.0, Moneda.USD),
                captor.getValue());
    }

    @Test
    void ejecutar_firmaCorta_noAplicaCategoriaNiPrecio() {
      
        when(libroRepository.buscarLibros(any(FiltroLibros.class), anyInt(), anyInt()))
                .thenReturn(new Pagina<>(List.of(), 0, 0, 0, 10));

       
        listarLibros.ejecutar("rio", EstadoLibro.AGOTADO, 0, 10);

       
        verify(libroRepository).buscarLibros(FiltroLibros.porTituloYEstado("rio", EstadoLibro.AGOTADO), 0, 10);
    }

    @Test
    void ejecutar_paginaNegativa_lanzaReglaDominioYNoConsulta() {
        
        assertThrows(ReglaDominioException.class,
                () -> listarLibros.ejecutar(null, null, -1, 10));
        verify(libroRepository, never()).buscarLibros(any(FiltroLibros.class), anyInt(), anyInt());
    }

    @Test
    void ejecutar_tamanioCero_lanzaReglaDominioYNoConsulta() {
        
        assertThrows(ReglaDominioException.class,
                () -> listarLibros.ejecutar(null, null, 0, 0));
        verify(libroRepository, never()).buscarLibros(any(FiltroLibros.class), anyInt(), anyInt());
    }

    @Test
    void ejecutar_tamanioMayorAlMaximo_lanzaReglaDominioYNoConsulta() {
        
        assertThrows(ReglaDominioException.class,
                () -> listarLibros.ejecutar(null, null, 0, 21));
        verify(libroRepository, never()).buscarLibros(any(FiltroLibros.class), anyInt(), anyInt());
    }

    @Test
    void ejecutar_rangoDePrecioInvertido_lanzaReglaDominioYNoConsulta() {
        
        assertThrows(ReglaDominioException.class, () ->
                listarLibros.ejecutar(null, null, null, 50.0, 10.0, Moneda.USD, 0, 10));
        verify(libroRepository, never()).buscarLibros(any(FiltroLibros.class), anyInt(), anyInt());
    }

    @Test
    void ejecutar_precioSinMoneda_lanzaReglaDominioYNoConsulta() {
        
        assertThrows(ReglaDominioException.class, () ->
                listarLibros.ejecutar(null, null, null, 10.0, null, null, 0, 10));
        verify(libroRepository, never()).buscarLibros(any(FiltroLibros.class), anyInt(), anyInt());
    }
}