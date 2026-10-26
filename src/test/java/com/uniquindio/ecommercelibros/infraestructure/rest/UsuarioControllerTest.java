package com.uniquindio.ecommercelibros.infraestructure.rest;

import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ActualizarUsuario;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ConsultarUsuarioCorreo;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ConsultarUsuarioID;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.EliminarUsuario;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ListarUsuarios;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.RegistrarUsuario;
import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import com.uniquindio.ecommercelibros.infrastructure.rest.UsuarioController;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.UsuarioMapper;
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

@WebMvcTest(UsuarioController.class)
@Import(UsuarioMapper.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarUsuario registrarUsuario;

    @MockitoBean
    private ConsultarUsuarioID consultarUsuarioID;

    @MockitoBean
    private ConsultarUsuarioCorreo consultarUsuarioCorreo;

    @MockitoBean
    private ListarUsuarios listarUsuarios;

    @MockitoBean
    private ActualizarUsuario actualizarUsuario;

    @MockitoBean
    private EliminarUsuario eliminarUsuario;

    @Test
    @DisplayName("POST válido responde 201 con Location y nunca expone la contraseña")
    void registrarUsuario_responde201() throws Exception {
        when(registrarUsuario.registrar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("juan@mail.com", "Clave123")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.idUsuario").exists())
                .andExpect(jsonPath("$.correo").value("juan@mail.com"))
                .andExpect(jsonPath("$.estadoUsuario").value("ACTIVO"))
                .andExpect(jsonPath("$.contrasenia").doesNotExist());
    }

    @Test
    @DisplayName("POST con correo ya registrado responde 400")
    void registrarCorreoDuplicado_responde400() throws Exception {
        when(registrarUsuario.registrar(any()))
                .thenThrow(new ReglaDominioException("Ya existe un usuario registrado con ese correo"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("juan@mail.com", "Clave123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un usuario registrado con ese correo"));
    }

    @Test
    @DisplayName("POST con correo mal formado responde 400 y no llega al caso de uso")
    void registrarCorreoInvalido_responde400() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("correo-invalido", "Clave123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.correo").exists());

        verify(registrarUsuario, never()).registrar(any());
    }

    @Test
    @DisplayName("POST con contraseña sin mayúscula ni número responde 400 y no registra")
    void registrarContraseniaInsegura_responde400() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("juan@mail.com", "clavesegura")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                        .value("La contraseña debe tener mínimo 8 caracteres, una mayúscula y un número."));

        verify(registrarUsuario, never()).registrar(any());
    }

    @Test
    @DisplayName("GET de un usuario existente responde 200")
    void consultarUsuarioExistente_responde200() throws Exception {
        when(consultarUsuarioID.consultar("user-1")).thenReturn(Optional.of(crearUsuario("user-1")));

        mockMvc.perform(get("/api/usuarios/{idUsuario}", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value("user-1"))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$.rolUsuario").value("CLIENTE"));
    }

    @Test
    @DisplayName("GET de un usuario que no existe responde 404")
    void consultarUsuarioInexistente_responde404() throws Exception {
        when(consultarUsuarioID.consultar("no-existe")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/{idUsuario}", "no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET por correo responde 200")
    void consultarPorCorreo_responde200() throws Exception {
        when(consultarUsuarioCorreo.buscar("juan@mail.com")).thenReturn(Optional.of(crearUsuario("user-1")));

        mockMvc.perform(get("/api/usuarios/correo/{correo}", "juan@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("juan@mail.com"));
    }

    @Test
    @DisplayName("GET de la lista de usuarios responde 200")
    void listarUsuarios_responde200() throws Exception {
        when(listarUsuarios.listar()).thenReturn(List.of(crearUsuario("user-1"), crearUsuario("user-2")));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT del correo responde 204")
    void actualizarCorreo_responde204() throws Exception {
        mockMvc.perform(put("/api/usuarios/{idUsuario}/correo", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "correo": "nuevo@mail.com" }
                                """))
                .andExpect(status().isNoContent());

        verify(actualizarUsuario).actualizarCorreo("user-1", new Correo("nuevo@mail.com"));
    }

    @Test
    @DisplayName("PUT de la contraseña con la actual incorrecta responde 400")
    void cambiarContraseniaIncorrecta_responde400() throws Exception {
        doThrow(new ReglaDominioException("La contraseña actual no es correcta."))
                .when(actualizarUsuario)
                .cambiarContrasenia("user-1", "OtraClave1", "NuevaClave456");

        mockMvc.perform(put("/api/usuarios/{idUsuario}/contrasenia", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "contraseniaActual": "OtraClave1", "nuevaContrasenia": "NuevaClave456" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La contraseña actual no es correcta."));
    }

    @Test
    @DisplayName("DELETE desactiva al usuario y responde 204")
    void eliminarUsuario_responde204() throws Exception {
        mockMvc.perform(delete("/api/usuarios/{idUsuario}", "user-1"))
                .andExpect(status().isNoContent());

        verify(eliminarUsuario).eliminar("user-1");
    }

    @Test
    @DisplayName("DELETE de un usuario que no existe responde 404")
    void eliminarUsuarioInexistente_responde404() throws Exception {
        doThrow(new RecursoNoEncontradoException("El usuario no existe"))
                .when(eliminarUsuario)
                .eliminar("no-existe");

        mockMvc.perform(delete("/api/usuarios/{idUsuario}", "no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("El usuario no existe"));
    }

    private Usuario crearUsuario(String id) {
        return Usuario.crear(id, "Juan Perez", new Correo("juan@mail.com"), "Clave123",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }

    private String json(String correo, String contrasenia) {
        return """
                {
                  "nombre": "Juan Perez",
                  "correo": "%s",
                  "contrasenia": "%s",
                  "telefono": "3001234567",
                  "direccion": "Calle 1",
                  "rolUsuario": "CLIENTE"
                }
                """.formatted(correo, contrasenia);
    }
}