package com.uniquindio.ecommercelibros.infraestructure.rest;

import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.RegistrarLibroUseCase;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.infrastructure.rest.LibroController;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class LibroControllerCasosTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarLibroUseCase registrarLibro;

    @MockitoBean
    private ConsultarLibroID consultarLibroID;

    @MockitoBean
    private LibroMapper mapper;

    @Test
    @DisplayName("GET de un libro que no existe responde 404 y nunca se registra nada")
    void consultarLibroInexistente_responde404_yNoRegistra() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        when(consultarLibroID.ConsultarLibroPorID(id)).thenReturn(Optional.empty());

        // Act + Assert
        mockMvc.perform(get("/api/libros/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").exists());

        verify(registrarLibro, never()).registrarLibro(any());
    }

    @Test
    @DisplayName("Una regla de dominio rota al registrar responde 400 y nunca se registra el libro")
    void registrarConReglaRota_responde400_yNoRegistra() throws Exception {
        // Arrange: el dominio rechaza los datos al construir los value objects
        when(mapper.toDomain(any()))
                .thenThrow(new ReglaDominioException("El precio no puede ser negativo"));

        // Act + Assert
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensaje").value("El precio no puede ser negativo"));

        verify(registrarLibro, never()).registrarLibro(any());
    }

    private String json() {
        return """
                {
                  "titulo": "Fundamentos de programación",
                  "idAutor": "%s",
                  "isbn": "9780306406157",
                  "descripcion": "Descripción de prueba",
                  "numeroPaginas": 320,
                  "precio": 45000.50,
                  "moneda": "COP",
                  "stock": 12,
                  "edicion": "Primera edición",
                  "idEditorial": "%s",
                  "fechaPublicacion": "2020-05-10",
                  "categoria": "NOMBRE_REAL_DE_CATEGORIA",
                  "imagen": "https://imagenes.test/libro.png",
                  "estado": "NOMBRE_REAL_DE_ESTADO"
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());
    }
}