package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeAutor;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class RegistrarAutorTest {

    private AutorRepository autorRepository;
    private VerificadorDeAutor verificadorDeAutor;
    private RegistrarAutor registrarAutor;

    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
        verificadorDeAutor = mock(VerificadorDeAutor.class);
        registrarAutor = new RegistrarAutor(autorRepository, verificadorDeAutor);
    }

    @Test
    void registrar_conDatosValidos_guardaElAutorNoEliminado() {
    
        Autor autor = registrarAutor.registrar("autor-1", "Gabriel Garcia Marquez", "Bio", Nacionalidad.COLOMBIANA);

        
        assertEquals("autor-1", autor.getIdAutor());
        assertEquals(List.of(Nacionalidad.COLOMBIANA), autor.getNacionalidades());
        assertFalse(autor.isEliminado());
        verify(verificadorDeAutor).verificarQueAutorNoExiste("autor-1");
        verify(autorRepository).guardarAutor(autor);
    }

    @Test
    void registrar_idRepetido_lanzaRecursoDuplicadoYNuncaGuarda() {
        
        doThrow(new RecursoDuplicadoException("Ya existe un autor registrado con ese id"))
                .when(verificadorDeAutor).verificarQueAutorNoExiste("autor-1");

       
        assertThrows(RecursoDuplicadoException.class, () ->
                registrarAutor.registrar("autor-1", "Gabriel Garcia Marquez", "Bio", Nacionalidad.COLOMBIANA));
        verify(autorRepository, never()).guardarAutor(any());
    }

    @Test
    void registrar_sinNacionalidad_lanzaReglaDominioYNuncaGuarda() {
       
        assertThrows(ReglaDominioException.class, () ->
                registrarAutor.registrar("autor-1", "Gabriel Garcia Marquez", "Bio", null));
        verify(autorRepository, never()).guardarAutor(any());
    }
}