package com.uniquindio.ecommercelibros.security;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SeguridadDuenoRecursoTest {

    @Autowired private MockMvc mockMvc;

    private static RequestPostProcessor con(String rol, String uid) {
        return jwt().authorities(new SimpleGrantedAuthority(rol)).jwt(j -> j.claim("uid", uid));
    }

    @Test
    void listarCarritos_sinToken_da401ConJson() throws Exception {
        mockMvc.perform(get("/api/carritos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void listarCarritos_comoCliente_da403ConJson() throws Exception {
        mockMvc.perform(get("/api/carritos").with(con("CLIENTE", "cliente-1")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void listarCarritos_comoAdmin_da200() throws Exception {
        mockMvc.perform(get("/api/carritos").with(con("ADMINISTRADOR", "admin-1")))
                .andExpect(status().isOk());
    }

    @Test
    void carritoAjeno_unIntrusoRecibe403_elDuenoYElAdminRecibenAcceso() throws Exception {
        String idCarrito = crearCarrito("dueno-1");

        mockMvc.perform(get("/api/carritos/{id}", idCarrito).with(con("CLIENTE", "intruso-1")))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/carritos/{id}/confirmar", idCarrito).with(con("CLIENTE", "intruso-1")))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/carritos/{id}", idCarrito).with(con("CLIENTE", "intruso-1")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/carritos/{id}", idCarrito).with(con("CLIENTE", "dueno-1")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/carritos/{id}", idCarrito).with(con("ADMINISTRADOR", "admin-1")))
                .andExpect(status().isOk());
    }

    @Test
    void consultarCarritoPorUsuario_deOtro_da403_yElPropioNoEs403() throws Exception {
        mockMvc.perform(get("/api/carritos").param("idUsuario", "dueno-2").with(con("CLIENTE", "intruso-1")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/carritos").param("idUsuario", "dueno-2").with(con("CLIENTE", "dueno-2")))
                .andExpect(result -> assertNotEquals(403, result.getResponse().getStatus()));
    }

    @Test
    void ventasDeOtroUsuario_da403() throws Exception {
        mockMvc.perform(get("/api/ventas").param("idUsuario", "otro-usuario").with(con("CLIENTE", "cliente-1")))
                .andExpect(status().isForbidden());
    }

    @Test
    void ventasDelPropioUsuario_noEs403() throws Exception {
        mockMvc.perform(get("/api/ventas").param("idUsuario", "cliente-1").with(con("CLIENTE", "cliente-1")))
                .andExpect(result -> assertNotEquals(403, result.getResponse().getStatus()));
    }

    @Test
    void usuarioAjeno_da403_yElPropioNoEs403() throws Exception {
        String otro = UUID.randomUUID().toString();
        String propio = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/usuarios/{id}", otro).with(con("CLIENTE", propio)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/usuarios/{id}", propio).with(con("CLIENTE", propio)))
                .andExpect(result -> assertNotEquals(403, result.getResponse().getStatus()));
    }

    @Test
    void listarUsuarios_comoCliente_da403() throws Exception {
        mockMvc.perform(get("/api/usuarios").with(con("CLIENTE", "cliente-1")))
                .andExpect(status().isForbidden());
    }

    @Test
    void proveedor_puedeCrearLibros_peroUnClienteNo() throws Exception {
        mockMvc.perform(post("/api/libros").with(con("PROVEEDOR", "proveedor-1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro("9780000000701")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/libros").with(con("CLIENTE", "cliente-1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro("9780000000702")))
                .andExpect(status().isForbidden());
    }

    @Test
    void borrarAutores_esSoloDelAdmin_proveedorRecibe403() throws Exception {
        String idAutor = UUID.randomUUID().toString();

        mockMvc.perform(delete("/api/autores/{id}", idAutor).with(con("PROVEEDOR", "proveedor-1")))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/autores/{id}", idAutor).with(con("ADMINISTRADOR", "admin-1")))
                .andExpect(result -> assertNotEquals(403, result.getResponse().getStatus()));
    }

    private String crearCarrito(String uidDueno) throws Exception {
        MvcResult r = mockMvc.perform(post("/api/carritos").with(con("CLIENTE", uidDueno)))
                .andExpect(status().isCreated())
                .andReturn();
        return JsonPath.read(r.getResponse().getContentAsString(), "$.idCarrito");
    }

    private static String cuerpoLibro(String isbn) {
        return """
                {
                  "titulo": "Libro de seguridad",
                  "idAutor": "autor-seguridad",
                  "isbn": "%s",
                  "descripcion": "Libro para pruebas de seguridad",
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
