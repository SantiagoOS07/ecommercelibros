package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.usecase.libro.*;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
@AutoConfigureMockMvc(addFilters = false)
class LibroControllerErroresTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarLibroUseCase registrarLibro;

    @MockitoBean
    private ConsultarLibroID consultarLibroID;

    @MockitoBean
    private ListarLibros listarLibros;

    @MockitoBean
    private LibroMapper mapper;

    @MockitoBean
    private BuscarLibroISBN buscarLibroISBN;

    @MockitoBean
    private BuscarLibroNombre buscarLibroNombre;

    @MockitoBean
    private BuscarLibroAutor buscarLibroAutor;

    @MockitoBean
    private ActualizarLibro actualizarLibro;

    @MockitoBean
    private EliminarLibro eliminarLibro;

    @Test
    void deberiaResponder404CuandoElLibroNoExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(consultarLibroID.ConsultarLibroPorID(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/libros/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(consultarLibroID).ConsultarLibroPorID(id);
        verify(registrarLibro, never()).registrarLibro(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void deberiaResponder400CuandoSeRompeUnaReglaDeNegocio() throws Exception {
        when(registrarLibro.registrarLibro(any()))
                .thenThrow(new ReglaDominioException("Ya existe un libro registrado con ese ISBN"));

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoValido()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un libro registrado con ese ISBN"));

        verify(registrarLibro).registrarLibro(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void noDeberiaLlamarAlCasoDeUsoSiLosDatosSonInvalidos() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores").exists());

        verify(mapper, never()).toDomain(any());
        verify(registrarLibro, never()).registrarLibro(any());
    }

    @Test
    void deberiaResponder400CuandoElIdDelLibroNoEsUnUuid() throws Exception {
        mockMvc.perform(get("/api/libros/{id}", "no-es-un-uuid"))
                .andExpect(status().isBadRequest());

        verify(consultarLibroID, never()).ConsultarLibroPorID(any());
    }

    private static String cuerpoValido() {
        return """
                {
                  "titulo": "Libro de prueba",
                  "idAutor": "autor-1",
                  "isbn": "9780000000011",
                  "descripcion": "Descripcion",
                  "numeroPaginas": 100,
                  "precio": 25000.0,
                  "moneda": "COP",
                  "stock": 5,
                  "edicion": "Primera edicion",
                  "idEditorial": "%s",
                  "fechaPublicacion": "2020-05-10",
                  "categoria": "NOVELA",
                  "imagen": "https://imagenes.test/libro.png",
                  "estado": "DISPONIBLE"
                }
                """.formatted(UUID.randomUUID());
    }
}