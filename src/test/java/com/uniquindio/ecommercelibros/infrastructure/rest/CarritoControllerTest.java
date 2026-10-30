package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.CarritoResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.*;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.BuscarLibroISBN;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarritoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CarritoController controller;

    @MockitoBean private RegistrarCarrito registrarCarrito;
    @MockitoBean private AgregarItemCarrito agregarItemCarrito;
    @MockitoBean private ActualizarCantidadItemCarrito actualizarCantidadItemCarrito;
    @MockitoBean private EliminarItemCarrito eliminarItemCarrito;
    @MockitoBean private ConfirmarCarrito confirmarCarrito;
    @MockitoBean private CancelarConfirmacionCarrito cancelarConfirmacionCarrito;
    @MockitoBean private CompletarVentaCarrito completarVentaCarrito;
    @MockitoBean private ConsultarCarritoPorID consultarCarritoPorID;
    @MockitoBean private ConsultarCarritoPorUsuario consultarCarritoPorUsuario;
    @MockitoBean private ListarCarritos listarCarritos;
    @MockitoBean private EliminarCarrito eliminarCarrito;
    @MockitoBean private ConsultarLibroID consultarLibroID;
    @MockitoBean private BuscarLibroISBN buscarLibroISBN;
    @MockitoBean private CarritoMapper mapper;

    @Test
    void crear_conToken_usaElUidDelTokenYResponde201() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/carritos");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        try {
            Jwt jwt = Jwt.withTokenValue("token-falso").header("alg", "none").claim("uid", "usuario123").build();
            Carrito carrito = Carrito.crear("usuario123");
            when(registrarCarrito.ejecutar("usuario123")).thenReturn(carrito);
            when(mapper.toResponse(carrito, Map.of())).thenReturn(respuesta(carrito));

            // Act
            ResponseEntity<CarritoResponse> resultado = controller.crear(jwt);

            // Assert
            assertEquals(201, resultado.getStatusCode().value());
            assertNotNull(resultado.getHeaders().getLocation());
            assertEquals("usuario123", resultado.getBody().idUsuario());
            verify(registrarCarrito).ejecutar("usuario123");
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    void crear_conCarritoActivo_propagaElDuplicado() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("token-falso").header("alg", "none").claim("uid", "usuario123").build();
        when(registrarCarrito.ejecutar("usuario123"))
                .thenThrow(new RecursoDuplicadoException("El usuario ya tiene un carrito activo"));

        // Act + Assert (el 409 lo cubre el test de integración)
        assertThrows(RecursoDuplicadoException.class, () -> controller.crear(jwt));
    }

    @Test
    void consultarPorId_existente_responde200() throws Exception {
        Carrito carrito = Carrito.crear("usuario123");
        when(consultarCarritoPorID.ejecutar(carrito.getIdCarrito())).thenReturn(Optional.of(carrito));
        when(mapper.toResponse(eq(carrito), anyMap())).thenReturn(respuesta(carrito));

        mockMvc.perform(get("/api/carritos/{id}", carrito.getIdCarrito()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCarrito").value(carrito.getIdCarrito().toString()));
    }

    @Test
    void consultarPorId_inexistente_responde404() throws Exception {
        UUID id = UUID.randomUUID();
        when(consultarCarritoPorID.ejecutar(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/carritos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void consultarPorId_conIdQueNoEsUuid_responde400() throws Exception {
        mockMvc.perform(get("/api/carritos/{id}", "no-es-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_responde200ConLaLista() throws Exception {
        Carrito carrito = Carrito.crear("usuario123");
        when(listarCarritos.ejecutar()).thenReturn(List.of(carrito));
        when(mapper.toResponse(eq(carrito), anyMap())).thenReturn(respuesta(carrito));

        mockMvc.perform(get("/api/carritos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void agregarItem_libroDisponible_responde204() throws Exception {
        UUID idCarrito = UUID.randomUUID();
        Libro libro = libro(EstadoLibro.DISPONIBLE);
        when(consultarLibroID.ConsultarLibroPorID(libro.getIdLibro())).thenReturn(Optional.of(libro));

        mockMvc.perform(post("/api/carritos/{id}/items", idCarrito)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoItem(libro.getIdLibro(), 2)))
                .andExpect(status().isNoContent());

        verify(agregarItemCarrito).ejecutar(idCarrito, libro.getIsbn(), 2, libro.getPrecio(), libro.getStock());
    }

    @Test
    void agregarItem_libroInexistente_responde404YNoAgrega() throws Exception {
        UUID idLibro = UUID.randomUUID();
        when(consultarLibroID.ConsultarLibroPorID(idLibro)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/carritos/{id}/items", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoItem(idLibro, 1)))
                .andExpect(status().isNotFound());

        verifyNoInteractions(agregarItemCarrito);
    }

    @Test
    void agregarItem_libroAgotado_responde400YNoAgrega() throws Exception {
        Libro agotado = libro(EstadoLibro.AGOTADO);
        when(consultarLibroID.ConsultarLibroPorID(agotado.getIdLibro())).thenReturn(Optional.of(agotado));

        mockMvc.perform(post("/api/carritos/{id}/items", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoItem(agotado.getIdLibro(), 1)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(agregarItemCarrito);
    }

    @Test
    void agregarItem_conCantidadCero_responde400() throws Exception {
        mockMvc.perform(post("/api/carritos/{id}/items", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoItem(UUID.randomUUID(), 0)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(agregarItemCarrito);
    }

    @Test
    void actualizarCantidad_sinParametroCantidad_responde400() throws Exception {
        mockMvc.perform(put("/api/carritos/{id}/items/{idLibro}", UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarItem_libroInexistente_responde404() throws Exception {
        UUID idLibro = UUID.randomUUID();
        when(consultarLibroID.ConsultarLibroPorID(idLibro)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/carritos/{id}/items/{idLibro}", UUID.randomUUID(), idLibro))
                .andExpect(status().isNotFound());
    }

    @Test
    void confirmar_responde204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(put("/api/carritos/{id}/confirmar", id))
                .andExpect(status().isNoContent());

        verify(confirmarCarrito).ejecutar(id);
    }

    @Test
    void confirmar_carritoVacio_responde400() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ReglaDominioException("El carrito no se puede confirmar porque está vacío."))
                .when(confirmarCarrito).ejecutar(id);

        mockMvc.perform(put("/api/carritos/{id}/confirmar", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El carrito no se puede confirmar porque está vacío."));
    }

    @Test
    void completarVenta_reglaRota_responde400() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ReglaDominioException("No hay stock suficiente"))
                .when(completarVentaCarrito).ejecutar(id);

        mockMvc.perform(put("/api/carritos/{id}/completar-venta", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_responde204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/carritos/{id}", id))
                .andExpect(status().isNoContent());

        verify(eliminarCarrito).ejecutar(id);
    }

    @Test
    void eliminar_carritoNoCreado_responde400() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ReglaDominioException("Solo se puede eliminar un carrito en estado CREADO."))
                .when(eliminarCarrito).ejecutar(id);

        mockMvc.perform(delete("/api/carritos/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    void limpiarContextoDeSeguridad() {
        SecurityContextHolder.clearContext();
    }

    private static void autenticarComo(String uid) {
        Jwt jwt = Jwt.withTokenValue("token-falso").header("alg", "none").claim("uid", uid).build();
        SecurityContext contexto = SecurityContextHolder.createEmptyContext();
        contexto.setAuthentication(new JwtAuthenticationToken(jwt));
        SecurityContextHolder.setContext(contexto);
    }

    private static String cuerpoItem(UUID idLibro, int cantidad) {
        return """
                { "idLibro": "%s", "cantidad": %d }
                """.formatted(idLibro, cantidad);
    }

    private static CarritoResponse respuesta(Carrito carrito) {
        return new CarritoResponse(carrito.getIdCarrito(), carrito.getIdUsuario(),
                List.of(), carrito.getEstado(), 0.0);
    }

    private static Libro libro(EstadoLibro estado) {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN("9783161484100"),
                "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP),
                new Stock(10), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA,
                "imagen.jpg", estado);
    }
}