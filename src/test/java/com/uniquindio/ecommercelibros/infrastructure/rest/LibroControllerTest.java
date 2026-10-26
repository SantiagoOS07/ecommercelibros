package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.RegistrarLibroRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.RegistrarLibroUseCase;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibroControllerTest {

    @Mock
    private RegistrarLibroUseCase registrarLibro;

    @Mock
    private ConsultarLibroID consultarLibroID;

    @Mock
    private LibroMapper mapper;

    @InjectMocks
    private LibroController controller;

    @BeforeEach
    void configurarSolicitud() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/libros");

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(request)
        );
    }

    @Test
    void registrarLibro_debeRetornar201() {
        // Arrange
        RegistrarLibroRequest request = mock(RegistrarLibroRequest.class);
        Libro libroDominio = mock(Libro.class);
        Libro libroRegistrado = mock(Libro.class);
        LibroResponse response = mock(LibroResponse.class);
        UUID id = UUID.randomUUID();

        when(mapper.toDomain(request)).thenReturn(libroDominio);
        when(registrarLibro.registrarLibro(libroDominio)).thenReturn(libroRegistrado);
        when(libroRegistrado.getIdLibro()).thenReturn(id);
        when(mapper.toResponse(libroRegistrado)).thenReturn(response);

        // Act
        ResponseEntity<LibroResponse> resultado = controller.registrar(request);

        // Assert
        assertEquals(201, resultado.getStatusCode().value());
        assertSame(response, resultado.getBody());
        assertEquals("/api/libros/" + id, resultado.getHeaders().getLocation().getPath());

        verify(mapper).toDomain(request);
        verify(registrarLibro).registrarLibro(libroDominio);
        verify(mapper).toResponse(libroRegistrado);
    }

    @Test
    void consultarPorId_debeRetornar200() {
        UUID id = UUID.randomUUID();

        Libro libro = mock(Libro.class);
        LibroResponse response = mock(LibroResponse.class);

        when(consultarLibroID.ConsultarLibroPorID(id))
                .thenReturn(Optional.of(libro));

        when(mapper.toResponse(libro))
                .thenReturn(response);

        ResponseEntity<LibroResponse> resultado =
                controller.consultarPorId(id);

        assertEquals(200, resultado.getStatusCode().value());
        assertSame(response, resultado.getBody());

        verify(consultarLibroID).ConsultarLibroPorID(id);
        verify(mapper).toResponse(libro);
    }

    @Test
    void consultarPorId_libroInexistente_debeLanzarExcepcion() {
        UUID id = UUID.randomUUID();

        when(consultarLibroID.ConsultarLibroPorID(id))
                .thenReturn(Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> controller.consultarPorId(id)
        );

        verify(consultarLibroID).ConsultarLibroPorID(id);
        verifyNoInteractions(mapper);
    }
}
