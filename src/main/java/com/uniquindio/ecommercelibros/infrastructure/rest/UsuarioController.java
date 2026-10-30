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
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

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

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {

        Usuario usuario = registrarUsuario.registrar(
                request.nombre(),
                new Correo(request.correo()),
                request.contrasenia(),
                new Telefono(request.telefono()),
                new Direccion(request.direccion()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(usuario.getIdUsuario())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toResponse(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(listarUsuarios.listar()));
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> consultarPorId(@PathVariable String idUsuario) {

        Usuario usuario = consultarUsuarioID.consultar(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con ID: " + idUsuario
                ));

        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponse> consultarPorCorreo(@PathVariable String correo) {

        Usuario usuario = consultarUsuarioCorreo.buscar(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un usuario con el correo: " + correo
                ));

        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @PutMapping("/{idUsuario}/nombre")
    public ResponseEntity<Void> actualizarNombre(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarNombreUsuarioRequest request) {

        actualizarUsuario.actualizarNombre(idUsuario, request.nombre());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUsuario}/correo")
    public ResponseEntity<Void> actualizarCorreo(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarCorreoUsuarioRequest request) {

        actualizarUsuario.actualizarCorreo(idUsuario, new Correo(request.correo()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUsuario}/telefono")
    public ResponseEntity<Void> actualizarTelefono(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarTelefonoUsuarioRequest request) {

        actualizarUsuario.actualizarTelefono(idUsuario, new Telefono(request.telefono()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUsuario}/direccion")
    public ResponseEntity<Void> actualizarDireccion(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarDireccionUsuarioRequest request) {

        actualizarUsuario.actualizarDireccion(idUsuario, new Direccion(request.direccion()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUsuario}/rol")
    public ResponseEntity<Void> actualizarRol(
            @PathVariable String idUsuario,
            @Valid @RequestBody ActualizarRolUsuarioRequest request) {

        actualizarUsuario.actualizarRol(idUsuario, request.rolUsuario());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #idUsuario == authentication.principal.getClaimAsString('uid')")
    @PutMapping("/{idUsuario}/contrasenia")
    public ResponseEntity<Void> cambiarContrasenia(
            @PathVariable String idUsuario,
            @Valid @RequestBody CambiarContraseniaUsuarioRequest request) {

        actualizarUsuario.cambiarContrasenia(idUsuario, request.contraseniaActual(), request.nuevaContrasenia());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUsuario}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable String idUsuario) {

        actualizarUsuario.reactivar(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable String idUsuario) {

        eliminarUsuario.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}