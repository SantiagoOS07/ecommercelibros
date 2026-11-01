package com.uniquindio.ecommercelibros.infrastructure.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.uniquindio.ecommercelibros.aplication.usecase.editorial.ConsultarEditorialID;
import com.uniquindio.ecommercelibros.aplication.usecase.editorial.ListarEditoriales;
import com.uniquindio.ecommercelibros.aplication.usecase.editorial.RegistrarEditorial;
import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.EditorialMapper;

@WebMvcTest(EditorialController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(EditorialMapper.class)
class EditorialControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private RegistrarEditorial registrarEditorial;
    @MockitoBean private ConsultarEditorialID consultarEditorialID;
    @MockitoBean private ListarEditoriales listarEditoriales;

    @Test
    void registrar_datosValidos_responde201ConLocation() throws Exception {
        
        Editorial editorial = Editorial.crear("Planeta", Pais.ESPANA, "Grupo editorial");
        when(registrarEditorial.registrar("Planeta", Pais.ESPANA, "Grupo editorial")).thenReturn(editorial);

        
        mockMvc.perform(post("/api/editoriales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Planeta")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/editoriales/" + editorial.getIdEditorial()))
                .andExpect(jsonPath("$.idEditorial").value(editorial.getIdEditorial().toString()))
                .andExpect(jsonPath("$.paisOrigen").value("ESPANA"));
    }

    @Test
    void registrar_sinNombre_responde400YNoLlegaAlCasoDeUso() throws Exception {
        
        mockMvc.perform(post("/api/editoriales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombreEditorial").exists());

        verify(registrarEditorial, never()).registrar(any(), any(), any());
    }

    @Test
    void registrar_nombreRepetido_responde409() throws Exception {
        
        when(registrarEditorial.registrar(any(), any(), any()))
                .thenThrow(new RecursoDuplicadoException("Ya existe una editorial registrada con ese nombre"));

        
        mockMvc.perform(post("/api/editoriales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("Planeta")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void consultarPorId_existente_responde200() throws Exception {
        
        Editorial editorial = Editorial.crear("Planeta", Pais.ESPANA, null);
        when(consultarEditorialID.consultar(editorial.getIdEditorial())).thenReturn(editorial);

        
        mockMvc.perform(get("/api/editoriales/{id}", editorial.getIdEditorial()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreEditorial").value("Planeta"));
    }

    @Test
    void consultarPorId_inexistente_responde404() throws Exception {
        
        UUID id = UUID.randomUUID();
        when(consultarEditorialID.consultar(id)).thenThrow(new RecursoNoEncontradoException("No existe"));

        
        mockMvc.perform(get("/api/editoriales/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void consultarPorId_idQueNoEsUuid_responde400() throws Exception {
        
        mockMvc.perform(get("/api/editoriales/{id}", "no-es-uuid"))
                .andExpect(status().isBadRequest());

        verify(consultarEditorialID, never()).consultar(any());
    }

    @Test
    void listar_conEditoriales_responde200() throws Exception {
        
        when(listarEditoriales.listar()).thenReturn(List.of(
                Editorial.crear("Planeta", Pais.ESPANA, null),
                Editorial.crear("Norma", Pais.COLOMBIA, null)));

        
        mockMvc.perform(get("/api/editoriales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private static String json(String nombre) {
        return """
                {
                  "nombreEditorial": "%s",
                  "paisOrigen": "ESPANA",
                  "descripcionEditorial": "Grupo editorial"
                }
                """.formatted(nombre);
    }
}