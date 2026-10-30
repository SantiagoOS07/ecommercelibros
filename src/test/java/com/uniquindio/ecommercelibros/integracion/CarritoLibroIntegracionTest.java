package com.uniquindio.ecommercelibros.integracion;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarritoLibroIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    private static RequestPostProcessor admin() {
        return jwt().authorities(new SimpleGrantedAuthority("ADMINISTRADOR"))
                .jwt(j -> j.claim("uid", "admin-integracion"));
    }

    private static RequestPostProcessor cliente(String uid) {
        return jwt().authorities(new SimpleGrantedAuthority("CLIENTE"))
                .jwt(j -> j.claim("uid", uid));
    }

    @Test
    void deberiaRegistrarUnLibroYAgregarloAUnCarritoNuevo() throws Exception {
        String idLibro = crearLibro("9780000000601", "Libro de integracion");
        String idCarrito = crearCarrito("usuario-integracion");

        mockMvc.perform(post("/api/carritos/{idCarrito}/items", idCarrito)
                        .with(cliente("usuario-integracion"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "idLibro": "%s", "cantidad": 2 }
                                """.formatted(idLibro)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/carritos/{idCarrito}", idCarrito)
                        .with(cliente("usuario-integracion")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value("usuario-integracion"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].tituloLibro").value("Libro de integracion"))
                .andExpect(jsonPath("$.items[0].cantidad").value(2))
                .andExpect(jsonPath("$.precioTotal").value(40.0));
    }

    @Test
    void deberiaRechazarUnLibroConIsbnRepetidoConConflicto() throws Exception { // [M-19]
        crearLibro("9780000000602", "Libro repetido");

        mockMvc.perform(post("/api/libros")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro("9780000000602", "Libro repetido")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un libro registrado con ese ISBN"));
    }

    @Test
    void libroEliminadoDebeDarNotFoundNoListarseYSeguirOcupandoSuIsbn() throws Exception {
        String titulo = "Libro para eliminar integracion";
        String idLibro = crearLibro("9780000000603", titulo);

        mockMvc.perform(get("/api/libros").param("palabra", titulo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        mockMvc.perform(delete("/api/libros/{id}", idLibro).with(admin()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/libros/{id}", idLibro))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/libros").param("palabra", titulo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

        mockMvc.perform(post("/api/libros")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro("9780000000603", titulo)))
                .andExpect(status().isConflict()); // [M-19]
    }

    @Test
    void eliminarUnLibroInexistenteDebeDarNotFound() throws Exception {
        mockMvc.perform(delete("/api/libros/{id}", UUID.randomUUID()).with(admin()))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarUnLibroDebeRetirarloDeLosCarritosCreado() throws Exception {
        String idLibro = crearLibro("9780000000604", "Libro en carrito creado");
        String idCarrito = crearCarrito("usuario-retiro");
        agregarItem(idCarrito, idLibro, "usuario-retiro");

        mockMvc.perform(delete("/api/libros/{id}", idLibro).with(admin()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/carritos/{id}", idCarrito).with(cliente("usuario-retiro")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    void noDebeEliminarUnLibroQueEstaEnUnCarritoProcesando() throws Exception {
        String idLibro = crearLibro("9780000000605", "Libro en carrito procesando");
        String idCarrito = crearCarrito("usuario-procesando");
        agregarItem(idCarrito, idLibro, "usuario-procesando");

        mockMvc.perform(put("/api/carritos/{id}/confirmar", idCarrito)
                        .with(cliente("usuario-procesando")))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/libros/{id}", idLibro).with(admin()))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/libros/{id}", idLibro))
                .andExpect(status().isOk());
    }

    @Test
    void carritoEliminadoDebeDarNotFoundYElUsuarioPuedeCrearOtro() throws Exception {
        String idCarrito = crearCarrito("usuario-recrea");

        mockMvc.perform(delete("/api/carritos/{id}", idCarrito).with(cliente("usuario-recrea")))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/carritos/{id}", idCarrito).with(cliente("usuario-recrea")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/carritos").with(cliente("usuario-recrea")))
                .andExpect(status().isCreated());
    }

    private String crearLibro(String isbn, String titulo) throws Exception {
        MvcResult r = mockMvc.perform(post("/api/libros")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoLibro(isbn, titulo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andReturn();
        return JsonPath.read(r.getResponse().getContentAsString(), "$.idLibro");
    }

    private String crearCarrito(String uid) throws Exception {
        MvcResult r = mockMvc.perform(post("/api/carritos").with(cliente(uid)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("CREADO"))
                .andReturn();
        return JsonPath.read(r.getResponse().getContentAsString(), "$.idCarrito");
    }

    private void agregarItem(String idCarrito, String idLibro, String uid) throws Exception {
        mockMvc.perform(post("/api/carritos/{idCarrito}/items", idCarrito)
                        .with(cliente(uid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "idLibro": "%s", "cantidad": 2 }
                                """.formatted(idLibro)))
                .andExpect(status().isNoContent());
    }

    private static String cuerpoLibro(String isbn, String titulo) {
        return """
                {
                  "titulo": "%s",
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
                """.formatted(titulo, isbn, UUID.randomUUID());
    }
}