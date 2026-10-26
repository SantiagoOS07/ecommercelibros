package com.uniquindio.ecommercelibros.integracion;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarritoLibroIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deberiaRegistrarUnLibroYAgregarloAUnCarritoNuevo() throws Exception {
        String isbn = "9780000000601";

        MvcResult libroCreado = mockMvc.perform(post("/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro(isbn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andReturn();
        String idLibro = JsonPath.read(libroCreado.getResponse().getContentAsString(), "$.idLibro");

        MvcResult carritoCreado = mockMvc.perform(post("/api/carritos")
                        .param("idUsuario", "usuario-integracion"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("CREADO"))
                .andReturn();
        String idCarrito = JsonPath.read(carritoCreado.getResponse().getContentAsString(), "$.idCarrito");

        mockMvc.perform(post("/api/carritos/{idCarrito}/items", idCarrito)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "idLibro": "%s", "cantidad": 2 }
                                """.formatted(idLibro)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/carritos/{idCarrito}", idCarrito))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value("usuario-integracion"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].tituloLibro").value("Libro de integracion"))
                .andExpect(jsonPath("$.items[0].cantidad").value(2))
                .andExpect(jsonPath("$.precioTotal").value(40.0));
    }

    @Test
    void deberiaRechazarUnLibroConIsbnRepetido() throws Exception {
        String isbn = "9780000000602";
        mockMvc.perform(post("/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro(isbn)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro(isbn)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un libro registrado con ese ISBN"));
    }

    private static String cuerpoLibro(String isbn) {
        return """
                {
                  "titulo": "Libro de integracion",
                  "idAutor": "autor-integracion",
                  "isbn": "%s",
                  "descripcion": "Libro para la prueba de integracion",
                  "numeroPaginas": 250,
                  "precio": 20.0,
                  "moneda": "USD",
                  "stock": 10,
                  "edicion": "Primera edicion",
                  "idEditorial": "%s",
                  "fechaPublicacion": "2020-05-10",
                  "categoria": "NOVELA",
                  "imagen": "https://imagenes.test/libro.png",
                  "estado": "DISPONIBLE"
                }
                """.formatted(isbn, UUID.randomUUID());
    }
}