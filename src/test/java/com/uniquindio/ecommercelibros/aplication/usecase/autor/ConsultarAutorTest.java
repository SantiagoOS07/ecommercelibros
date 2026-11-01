package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsultarAutorTest {

    private AutorRepository autorRepository;

    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
    }

    @Test
    void consultarPorId_autorExistente_loDevuelve() {
        
        Autor autor = crearAutor("autor-1");
        when(autorRepository.buscarPorIdAutor("autor-1")).thenReturn(Optional.of(autor));

        
        Optional<Autor> resultado = new ConsultarAutorID(autorRepository).consultar("autor-1");

        
        assertEquals(Optional.of(autor), resultado);
    }

    @Test
    void consultarPorId_autorInexistente_devuelveVacio() {
        
        when(autorRepository.buscarPorIdAutor("no-existe")).thenReturn(Optional.empty());

        
        Optional<Autor> resultado = new ConsultarAutorID(autorRepository).consultar("no-existe");

        
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorNombre_nombreRegistrado_devuelveElAutor() {
        
        Autor autor = crearAutor("autor-1");
        when(autorRepository.buscarPorNombre("Gabriel Garcia Marquez")).thenReturn(Optional.of(autor));

        
        Optional<Autor> resultado = new ConsultarAutorNombre(autorRepository).buscar("Gabriel Garcia Marquez");

        
        assertEquals(Optional.of(autor), resultado);
    }

    @Test
    void listar_conAutores_devuelveLosDelRepositorio() {
        
        when(autorRepository.listarTodos()).thenReturn(List.of(crearAutor("autor-1"), crearAutor("autor-2")));

        
        List<Autor> resultado = new ListarAutores(autorRepository).listar();

        
        assertEquals(2, resultado.size());
    }

    private Autor crearAutor(String id) {
        return Autor.crear(id, "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA);
    }
}