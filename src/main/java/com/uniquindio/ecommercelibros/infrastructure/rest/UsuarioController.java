package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.ActualizarCorreoUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.ActualizarDireccionUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.ActualizarNombreUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.ActualizarRolUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.ActualizarTelefonoUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.CambiarContraseniaUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.usuario.RegistrarUsuarioRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.usuario.UsuarioResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ActualizarUsuario;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ConsultarUsuarioCorreo;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ConsultarUsuarioID;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.EliminarUsuario;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.ListarUsuarios;
import com.uniquindio.ecommercelibros.aplication.usecase.usuario.RegistrarUsuario;
import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.UsuarioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios",
        description = "Registro público de clientes y gestión de cuentas. Cada usuario solo puede ver y modificar su propia cuenta; "
                + "listar, consultar por correo, cambiar rol, reactivar y eliminar son exclusivos del ADMINISTRADOR.")
public class UsuarioController {

    private static final String PROPIO_O_ADMIN =
            "hasAuthority('ADMINISTRADOR') or #idUsuario == authentication.principal.getClaimAsString('uid')";

    private final RegistrarUsuario registrarUsuario;
    private final ConsultarUsuarioID consultarUsuarioID;
    private final ConsultarUsuarioCorreo consultarUsuarioCorreo;
    private final ListarUsuarios listarUsuarios;
    private final ActualizarUsuario actualizarUsuario;
    private final EliminarUsuario eliminarUsuario;
    private final UsuarioMapper mapper;

    public UsuarioController(
            RegistrarUsuario registrarUsuario,
            ConsultarUsuarioID consultarUsuarioID,
            ConsultarUsuarioCorreo consultarUsuarioCorreo,
            ListarUsuarios listarUsuarios,
            ActualizarUsuario actualizarUsuario,
            EliminarUsuario eliminarUsuario,
            UsuarioMapper mapper) {

        this.registrarUsuario = registrarUsuario;
        this.consultarUsuarioID = consultarUsuarioID;
        this.consultarUsuarioCorreo = consultarUsuarioCorreo;
        this.listarUsuarios = listarUsuarios;
        this.actualizarUsuario = actualizarUsuario;
        this.eliminarUsuario = eliminarUsuario;
        this.mapper = mapper;
    }

    @Operation(summary = "Registrar usuario (público)",
            description = "Crea una cuenta con rol CLIENTE. La contraseña se guarda encriptada con BCrypt y nunca se devuelve.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o contraseña insegura", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese correo", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {

        Usuario usuario = registrarUsuario.registrar(
                request.nombre(),
                mapper.toCorreo(request.correo()),
                request.contrasenia(),
                mapper.toTelefono(request.telefono()),
                mapper.toDireccion(request.direccion()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(usuario.getIdUsuario())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toResponse(usuario));
    }

    @Operation(summary = "Listar usuarios", description = "Solo ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios"),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "No es ADMINISTRADOR", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(listarUsuarios.listar()));
    }

    @Operation(summary = "Consultar usuario por id", description = "El propio usuario o un ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es la cuenta de otro usuario", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize(PROPIO_O_ADMIN)
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> consultarPorId(@PathVariable String idUsuario) {

        Usuario usuario = consultarUsuarioID.consultar(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con ID: " + idUsuario
                ));

        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @Operation(summary = "Consultar usuario por correo", description = "Solo ADMINISTRADOR. No distingue mayúsculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "No es ADMINISTRADOR", content = @Content),
            @ApiResponse(responseCode = "404", description = "No hay un usuario con ese correo", content = @Content)
    })
    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponse> consultarPorCorreo(@PathVariable String correo) {

        Usuario usuario = consultarUsuarioCorreo.buscar(correo.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un usuario con el correo: " + correo
                ));

        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @Operation(summary = "Actualizar nombre", description = "El propio usuario o un ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Nombre actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido o usuario inactivo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es la cuenta de otro usuario", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize(PROPIO_O_ADMIN)
    @PutMapping("/{idUsuario}/nombre")
    public ResponseEntity<Void> actualizarNombre(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarNombreUsuarioRequest request) {

        actualizarUsuario.actualizarNombre(idUsuario, request.nombre());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar correo", description = "El propio usuario o un ADMINISTRADOR. El correo se guarda en minúsculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Correo actualizado"),
            @ApiResponse(responseCode = "400", description = "Correo inválido o usuario inactivo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es la cuenta de otro usuario", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content),
            @ApiResponse(responseCode = "409", description = "El correo ya pertenece a otro usuario", content = @Content)
    })
    @PreAuthorize(PROPIO_O_ADMIN)
    @PutMapping("/{idUsuario}/correo")
    public ResponseEntity<Void> actualizarCorreo(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarCorreoUsuarioRequest request) {

        actualizarUsuario.actualizarCorreo(idUsuario, mapper.toCorreo(request.correo()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar teléfono", description = "El propio usuario o un ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Teléfono actualizado"),
            @ApiResponse(responseCode = "400", description = "Teléfono vacío o usuario inactivo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es la cuenta de otro usuario", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize(PROPIO_O_ADMIN)
    @PutMapping("/{idUsuario}/telefono")
    public ResponseEntity<Void> actualizarTelefono(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarTelefonoUsuarioRequest request) {

        actualizarUsuario.actualizarTelefono(idUsuario, mapper.toTelefono(request.telefono()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar dirección", description = "El propio usuario o un ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dirección actualizada"),
            @ApiResponse(responseCode = "400", description = "Dirección vacía o usuario inactivo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es la cuenta de otro usuario", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize(PROPIO_O_ADMIN)
    @PutMapping("/{idUsuario}/direccion")
    public ResponseEntity<Void> actualizarDireccion(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarDireccionUsuarioRequest request) {

        actualizarUsuario.actualizarDireccion(idUsuario, mapper.toDireccion(request.direccion()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar rol", description = "Solo ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rol actualizado"),
            @ApiResponse(responseCode = "400", description = "Rol inválido o usuario inactivo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "No es ADMINISTRADOR", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping("/{idUsuario}/rol")
    public ResponseEntity<Void> actualizarRol(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarRolUsuarioRequest request) {

        actualizarUsuario.actualizarRol(idUsuario, request.rolUsuario());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar contraseña", description = "El propio usuario o un ADMINISTRADOR. Exige la contraseña actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contraseña actualizada"),
            @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta, nueva insegura o igual a la actual", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es la cuenta de otro usuario", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize(PROPIO_O_ADMIN)
    @PutMapping("/{idUsuario}/contrasenia")
    public ResponseEntity<Void> cambiarContrasenia(
            @PathVariable String idUsuario,
            @Valid @RequestBody CambiarContraseniaUsuarioRequest request) {

        actualizarUsuario.cambiarContrasenia(idUsuario, request.contraseniaActual(), request.nuevaContrasenia());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reactivar usuario", description = "Solo ADMINISTRADOR. Vuelve a ACTIVO un usuario eliminado lógicamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario reactivado"),
            @ApiResponse(responseCode = "400", description = "El usuario ya está activo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "No es ADMINISTRADOR", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping("/{idUsuario}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable String idUsuario) {

        actualizarUsuario.reactivar(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar usuario", description = "Solo ADMINISTRADOR. Eliminación lógica: el usuario queda INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario desactivado"),
            @ApiResponse(responseCode = "400", description = "El usuario ya está inactivo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "No es ADMINISTRADOR", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable String idUsuario) {

        eliminarUsuario.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}