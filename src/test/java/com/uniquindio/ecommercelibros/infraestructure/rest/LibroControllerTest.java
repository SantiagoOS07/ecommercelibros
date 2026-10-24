package com.uniquindio.ecommercelibros.infraestructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.RegistrarLibroRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.RegistrarLibroUseCase;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import com.uniquindio.ecommercelibros.infrastructure.rest.LibroController;
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
        RegistrarLibroRequest request =
                mock(RegistrarLibroRequest.class);

        Libro libroRequest = mock(Libro.class);
        Libro libroRegistrado = mock(Libro.class);
        LibroResponse response = mock(LibroResponse.class);

        UUID id = UUID.randomUUID();

        NombreLibro titulo = mock(NombreLibro.class);
        ISBN isbn = mock(ISBN.class);
        NumeroPaginas numeroPaginas = mock(NumeroPaginas.class);
        Precio precio = mock(Precio.class);
        Stock stock = mock(Stock.class);
        FechaPublicacion fechaPublicacion =
                mock(FechaPublicacion.class);

        String idAutor = "autor-123";
        String descripcion = "Libro de prueba";
        String edicion = "Primera";
        UUID idEditorial = UUID.randomUUID();
        CategoriaLibro categoria =
                CategoriaLibro.values()[0];
        EstadoLibro estado =
                EstadoLibro.values()[0];
        String imagen = "imagen.jpg";

        when(mapper.toDomain(request)).thenReturn(libroRequest);

        when(libroRequest.getTitulo()).thenReturn(titulo);
        when(libroRequest.getIdAutor()).thenReturn(idAutor);
        when(libroRequest.getIsbn()).thenReturn(isbn);
        when(libroRequest.getDescripcion()).thenReturn(descripcion);
        when(libroRequest.getNumeroPaginas()).thenReturn(numeroPaginas);
        when(libroRequest.getPrecio()).thenReturn(precio);
        when(libroRequest.getStock()).thenReturn(stock);
        when(libroRequest.getEdicion()).thenReturn(edicion);
        when(libroRequest.getIdEditorial()).thenReturn(idEditorial);
        when(libroRequest.getFechaPublicacion())
                .thenReturn(fechaPublicacion);
        when(libroRequest.getCategoria()).thenReturn(categoria);
        when(libroRequest.getImagen()).thenReturn(imagen);
        when(libroRequest.getEstado()).thenReturn(estado);

        when(registrarLibro.registrarLibro(
                titulo,
                idAutor,
                isbn,
                descripcion,
                numeroPaginas,
                precio,
                stock,
                edicion,
                idEditorial,
                fechaPublicacion,
                categoria,
                imagen,
                estado
        )).thenReturn(libroRegistrado);

        when(libroRegistrado.getIdLibro()).thenReturn(id);
        when(mapper.toResponse(libroRegistrado)).thenReturn(response);

        ResponseEntity<LibroResponse> resultado =
                controller.registrar(request);

        assertEquals(201, resultado.getStatusCode().value());
        assertSame(response, resultado.getBody());
        assertEquals(
                "/api/libros/" + id,
                resultado.getHeaders().getLocation().getPath()
        );

        verify(mapper).toDomain(request);

        verify(registrarLibro).registrarLibro(
                titulo,
                idAutor,
                isbn,
                descripcion,
                numeroPaginas,
                precio,
                stock,
                edicion,
                idEditorial,
                fechaPublicacion,
                categoria,
                imagen,
                estado
        );

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
