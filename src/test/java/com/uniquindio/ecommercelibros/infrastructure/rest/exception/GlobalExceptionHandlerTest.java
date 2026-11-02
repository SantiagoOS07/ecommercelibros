package com.uniquindio.ecommercelibros.infrastructure.rest.exception;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoDuplicadoException;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.exception.StockInsuficienteException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ControladorDePrueba())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }



    @Test
    void casillaVacia_responde400ConMensaje() throws Exception {
        mockMvc.perform(get("/prueba/casilla-vacia"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensaje").value("El campo es obligatorio"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void reglaDominio_responde422() throws Exception {
        mockMvc.perform(get("/prueba/regla"))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Regla rota"));
    }

    @Test
    void illegalArgument_responde400() throws Exception {
        mockMvc.perform(get("/prueba/argumento-invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dtoInvalido_responde400ConErroresPorCampo() throws Exception {
        mockMvc.perform(post("/prueba/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").value("El nombre es obligatorio"));
    }

    @Test
    void jsonIlegible_responde400() throws Exception {
        mockMvc.perform(post("/prueba/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ esto no es json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void parametroFaltante_responde400ConElNombreDelParametro() throws Exception {
        mockMvc.perform(get("/prueba/parametro"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Falta el parámetro obligatorio 'idUsuario'"));
    }

    @Test
    void uuidMalFormado_responde400() throws Exception {
        mockMvc.perform(get("/prueba/uuid/{id}", "no-es-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El parámetro 'id' tiene un formato inválido"));
    }



    @Test
    void autenticacionFallida_responde401ConMensajeGenerico() throws Exception {
        mockMvc.perform(get("/prueba/no-autenticado"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas o sesión no autenticada"));
    }

    @Test
    void accesoDenegado_responde403() throws Exception {
        mockMvc.perform(get("/prueba/sin-permiso"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

 

    @Test
    void recursoNoEncontrado_responde404() throws Exception {
        mockMvc.perform(get("/prueba/no-encontrado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe"));
    }

    @Test
    void noSuchElement_responde404() throws Exception {
        mockMvc.perform(get("/prueba/no-such-element"))
                .andExpect(status().isNotFound());
    }



    @Test
    void metodoNoPermitido_responde405() throws Exception {
        mockMvc.perform(put("/prueba/regla"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));
    }

    @Test
    void tipoDeContenidoNoSoportado_responde415() throws Exception {
        mockMvc.perform(post("/prueba/dto")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("hola"))
                .andExpect(status().isUnsupportedMediaType());
    }

 

    @Test
    void recursoDuplicado_responde409() throws Exception {
        mockMvc.perform(get("/prueba/duplicado"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Ya existe"));
    }

    @Test
    void integridadDeDatos_responde409SinExponerElDetalleSql() throws Exception {
        mockMvc.perform(get("/prueba/integridad"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje")
                        .value("La operación viola una restricción de datos (registro duplicado o referencia inválida)"));
    }

    @Test
    void stockInsuficiente_responde409() throws Exception {
        mockMvc.perform(get("/prueba/stock"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("No hay stock"));
    }

 

    @Test
    void errorInesperado_responde500SinExponerElMensajeInterno() throws Exception {
        mockMvc.perform(get("/prueba/inesperado"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje").value("Ocurrió un error interno"));
    }

 

    record DtoDePrueba(@NotBlank(message = "El nombre es obligatorio") String nombre) {}

    @RestController
    @RequestMapping("/prueba")
    static class ControladorDePrueba {

        @GetMapping("/casilla-vacia")
        void casillaVacia() { throw new CasillaVaciaException("El campo es obligatorio"); }

        @GetMapping("/regla")
        void regla() { throw new ReglaDominioException("Regla rota"); }

        @GetMapping("/argumento-invalido")
        void argumentoInvalido() { throw new IllegalArgumentException("Argumento inválido"); }

        @PostMapping("/dto")
        void dto(@Valid @RequestBody DtoDePrueba dto) { }

        @GetMapping("/parametro")
        void parametro(@RequestParam("idUsuario") String idUsuario) { }

        @GetMapping("/uuid/{id}")
        void uuid(@PathVariable("id") UUID id) { }

        @GetMapping("/no-autenticado")
        void noAutenticado() { throw new BadCredentialsException("Clave incorrecta"); }

        @GetMapping("/sin-permiso")
        void sinPermiso() { throw new AccessDeniedException("Denegado"); }

        @GetMapping("/no-encontrado")
        void noEncontrado() { throw new RecursoNoEncontradoException("No existe"); }

        @GetMapping("/no-such-element")
        void noSuchElement() { throw new NoSuchElementException("No hay valor"); }

        @GetMapping("/duplicado")
        void duplicado() { throw new RecursoDuplicadoException("Ya existe"); }

        @GetMapping("/integridad")
        void integridad() { throw new DataIntegrityViolationException("unique constraint violada en SQL"); }

        @GetMapping("/stock")
        void stock() { throw new StockInsuficienteException("No hay stock"); }

        @GetMapping("/inesperado")
        void inesperado() { throw new IllegalStateException("detalle interno"); }
    }
}