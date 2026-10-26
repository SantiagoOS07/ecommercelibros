package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.usecase.autor.ActualizarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ConsultarAutorID;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ConsultarAutorNombre;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.EliminarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ListarAutores;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.RegistrarAutor;
import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.AutorMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutorController.class)
@Import(AutorMapper.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarAutor registrarAutor;

    @MockitoBean
    private ConsultarAutorID consultarAutorID;

    @MockitoBean
    private ConsultarAutorNombre consultarAutorNombre;

    @MockitoBean
    private ListarAutores listarAutores;

    @MockitoBean
    private ActualizarAutor actualizarAutor;

    @MockitoBean
    private EliminarAutor eliminarAutor;

    @Test
    @DisplayName("POST válido responde 201 con Location y el autor registrado")
    void registrarAutor_responde201() throws Exception {
        when(registrarAutor.registrar("autor-1", "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA))
                .thenReturn(crearAutor("autor-1"));

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("autor-1", "Gabriel Garcia Marquez")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/autores/autor-1"))
                .andExpect(jsonPath("$.idAutor").value("autor-1"))
                .andExpect(jsonPath("$.nacionalidades[0]").value("COLOMBIANA"));
    }

    @Test
    @DisplayName("POST con un id ya registrado responde 400")
    void registrarAutorRepetido_responde400() throws Exception {
        when(registrarAutor.registrar(any(), any(), any(), any()))
                .thenThrow(new ReglaDominioException("Ya existe un autor registrado con ese id"));

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("autor-1", "Gabriel Garcia Marquez")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un autor registrado con ese id"));
    }

    @Test
    @DisplayName("POST sin nombre responde 400 y no llega al caso de uso")
    void registrarAutorSinNombre_responde400() throws Exception {
        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("autor-1", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombreAutor").exists());

        verify(registrarAutor, never()).registrar(any(), any(), any(), any());
    }

    @Test
    @DisplayName("GET de un autor que no existe responde 404")
    void consultarAutorInexistente_responde404() throws Exception {
        when(consultarAutorID.consultar("no-existe")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/autores/{idAutor}", "no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET por nombre responde 200")
    void consultarAutorPorNombre_responde200() throws Exception {
        when(consultarAutorNombre.buscar("Gabriel Garcia Marquez")).thenReturn(Optional.of(crearAutor("autor-1")));

        mockMvc.perform(get("/api/autores/nombre/{nombreAutor}", "Gabriel Garcia Marquez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreAutor").value("Gabriel Garcia Marquez"));
    }

    @Test
    @DisplayName("GET de la lista de autores responde 200")
    void listarAutores_responde200() throws Exception {
        when(listarAutores.listar()).thenReturn(List.of(crearAutor("autor-1"), crearAutor("autor-2")));

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT de la biografía responde 204")
    void actualizarBiografia_responde204() throws Exception {
        mockMvc.perform(put("/api/autores/{idAutor}/biografia", "autor-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "biografia": "Nueva biografia" }
                                """))
                .andExpect(status().isNoContent());

        verify(actualizarAutor).actualizarBiografia("autor-1", "Nueva biografia");
    }

    @Test
    @DisplayName("POST de una nacionalidad responde 204")
    void agregarNacionalidad_responde204() throws Exception {
        mockMvc.perform(post("/api/autores/{idAutor}/nacionalidades", "autor-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "nacionalidad": "ESPANOLA" }
                                """))
                .andExpect(status().isNoContent());

        verify(actualizarAutor).agregarNacionalidad("autor-1", Nacionalidad.ESPANOLA);
    }

    @Test
    @DisplayName("DELETE de una nacionalidad responde 204")
    void eliminarNacionalidad_responde204() throws Exception {
        mockMvc.perform(delete("/api/autores/{idAutor}/nacionalidades/{nacionalidad}", "autor-1", "ESPANOLA"))
                .andExpect(status().isNoContent());

        verify(actualizarAutor).eliminarNacionalidad("autor-1", Nacionalidad.ESPANOLA);
    }

    @Test
    @DisplayName("DELETE de una nacionalidad que no existe en el enum responde 400")
    void eliminarNacionalidadInvalida_responde400() throws Exception {
        mockMvc.perform(delete("/api/autores/{idAutor}/nacionalidades/{nacionalidad}", "autor-1", "MARCIANA"))
                .andExpect(status().isBadRequest());

        verify(actualizarAutor, never()).eliminarNacionalidad(any(), any());
    }

    @Test
    @DisplayName("DELETE de un autor con libros responde 400")
    void eliminarAutorConLibros_responde400() throws Exception {
        doThrow(new ReglaDominioException("No se puede eliminar un autor que tiene libros registrados"))
                .when(eliminarAutor)
                .eliminar("autor-1");

        mockMvc.perform(delete("/api/autores/{idAutor}", "autor-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("No se puede eliminar un autor que tiene libros registrados"));
    }

    @Test
    @DisplayName("DELETE de un autor sin libros responde 204")
    void eliminarAutor_responde204() throws Exception {
        mockMvc.perform(delete("/api/autores/{idAutor}", "autor-1"))
                .andExpect(status().isNoContent());

        verify(eliminarAutor).eliminar("autor-1");
    }

    private Autor crearAutor(String id) {
        return Autor.crear(id, "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA);
    }

    private String json(String idAutor, String nombreAutor) {
        return """
                {
                  "idAutor": "%s",
                  "nombreAutor": "%s",
                  "biografia": "Biografia",
                  "nacionalidad": "COLOMBIANA"
                }
                """.formatted(idAutor, nombreAutor);
    }
}