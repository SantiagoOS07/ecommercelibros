package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.ActualizarCantidadItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.AgregarItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.CarritoResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ActualizarCantidadItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.AgregarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.CancelarConfirmacionCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.CompletarVentaCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConfirmarCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConsultarCarritoPorID;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConsultarCarritoPorUsuario;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ListarCarritos;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ObtenerTitulosCarritos;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/carritos")
@Tag(name = "Carritos",
        description = "Gestión del carrito de compras. Requiere JWT. El dueño del carrito o un ADMINISTRADOR pueden operarlo.")
public class CarritoController {

    private final RegistrarCarrito registrarCarrito;
    private final AgregarItemCarrito agregarItemCarrito;
    private final ActualizarCantidadItemCarrito actualizarCantidadItemCarrito;
    private final EliminarItemCarrito eliminarItemCarrito;
    private final ConfirmarCarrito confirmarCarrito;
    private final CancelarConfirmacionCarrito cancelarConfirmacionCarrito;
    private final CompletarVentaCarrito completarVentaCarrito;
    private final ConsultarCarritoPorID consultarCarritoPorID;
    private final ConsultarCarritoPorUsuario consultarCarritoPorUsuario;
    private final ListarCarritos listarCarritos;
    private final EliminarCarrito eliminarCarrito;
    private final ObtenerTitulosCarritos obtenerTitulosCarritos;
    private final CarritoMapper mapper;

    public CarritoController(
            RegistrarCarrito registrarCarrito,
            AgregarItemCarrito agregarItemCarrito,
            ActualizarCantidadItemCarrito actualizarCantidadItemCarrito,
            EliminarItemCarrito eliminarItemCarrito,
            ConfirmarCarrito confirmarCarrito,
            CancelarConfirmacionCarrito cancelarConfirmacionCarrito,
            CompletarVentaCarrito completarVentaCarrito,
            ConsultarCarritoPorID consultarCarritoPorID,
            ConsultarCarritoPorUsuario consultarCarritoPorUsuario,
            ListarCarritos listarCarritos,
            EliminarCarrito eliminarCarrito,
            ObtenerTitulosCarritos obtenerTitulosCarritos,
            CarritoMapper mapper) {

        this.registrarCarrito = registrarCarrito;
        this.agregarItemCarrito = agregarItemCarrito;
        this.actualizarCantidadItemCarrito = actualizarCantidadItemCarrito;
        this.eliminarItemCarrito = eliminarItemCarrito;
        this.confirmarCarrito = confirmarCarrito;
        this.cancelarConfirmacionCarrito = cancelarConfirmacionCarrito;
        this.completarVentaCarrito = completarVentaCarrito;
        this.consultarCarritoPorID = consultarCarritoPorID;
        this.consultarCarritoPorUsuario = consultarCarritoPorUsuario;
        this.listarCarritos = listarCarritos;
        this.eliminarCarrito = eliminarCarrito;
        this.obtenerTitulosCarritos = obtenerTitulosCarritos;
        this.mapper = mapper;
    }

    @Operation(summary = "Crear carrito",
            description = "Crea el carrito del usuario autenticado (el uid sale del JWT). Un usuario solo puede tener un carrito activo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carrito creado"),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya tiene un carrito activo", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PostMapping
    public ResponseEntity<CarritoResponse> crear(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt) {

        Carrito carrito = registrarCarrito.ejecutar(jwt.getClaimAsString("uid"));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(carrito.getIdCarrito())
                .toUri();

        return ResponseEntity.created(location)
                .body(mapper.toResponse(carrito, Map.of()));
    }

    @Operation(summary = "Consultar carrito por id",
            description = "Devuelve el carrito con los títulos de sus libros. Solo el dueño o un ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito ni administrador", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "El carrito no existe", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @GetMapping("/{idCarrito}")
    public ResponseEntity<CarritoResponse> consultarPorId(@PathVariable UUID idCarrito) {

        Carrito carrito = consultarCarritoPorID.ejecutar(idCarrito)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el carrito con ID: " + idCarrito));

        return ResponseEntity.ok(mapper.toResponse(carrito, titulosDe(carrito)));
    }

    @Operation(summary = "Consultar carrito activo de un usuario",
            description = "Un cliente solo puede consultar el suyo; un ADMINISTRADOR cualquiera.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
            @ApiResponse(responseCode = "403", description = "Consulta el carrito de otro usuario", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "El usuario no tiene un carrito activo", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #idUsuario == authentication.principal.getClaimAsString('uid')")
    @GetMapping(params = "idUsuario")
    public ResponseEntity<CarritoResponse> consultarPorUsuario(@RequestParam String idUsuario) {

        Carrito carrito = consultarCarritoPorUsuario.ejecutar(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El usuario " + idUsuario + " no tiene un carrito activo"));

        return ResponseEntity.ok(mapper.toResponse(carrito, titulosDe(carrito)));
    }

    @Operation(summary = "Listar todos los carritos", description = "Solo ADMINISTRADOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de carritos"),
            @ApiResponse(responseCode = "403", description = "Requiere rol ADMINISTRADOR", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<CarritoResponse>> listar() {

        var carritos = listarCarritos.ejecutar();
        Map<String, String> titulos = obtenerTitulosCarritos.ejecutar(carritos); // 1 sola consulta

        return ResponseEntity.ok(
                carritos.stream().map(c -> mapper.toResponse(c, titulos)).toList());
    }

    @Operation(summary = "Agregar un libro al carrito",
            description = "Agrega el libro indicado con la cantidad pedida. El libro debe estar disponible para la venta.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ítem agregado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio (libro ya en el carrito, carrito no editable...)", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Carrito o libro inexistente", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PostMapping("/{idCarrito}/items")
    public ResponseEntity<Void> agregarItem(
            @PathVariable UUID idCarrito,
            @Valid @RequestBody AgregarItemCarritoRequest request) {

        agregarItemCarrito.ejecutar(idCarrito, request.idLibro(), request.cantidad());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar la cantidad de un libro del carrito",
            description = "La cantidad viaja en el cuerpo. Con cantidad 0 el ítem se elimina del carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cantidad actualizada"),
            @ApiResponse(responseCode = "400", description = "Cantidad ausente o negativa / regla de negocio", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Carrito o libro inexistente", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/items/{idLibro}")
    public ResponseEntity<Void> actualizarCantidad(
            @PathVariable UUID idCarrito,
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarCantidadItemCarritoRequest request) {

        actualizarCantidadItemCarrito.ejecutar(idCarrito, idLibro, request.cantidad());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Quitar un libro del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ítem eliminado"),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Carrito, libro o ítem inexistente", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @DeleteMapping("/{idCarrito}/items/{idLibro}")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable UUID idCarrito,
            @PathVariable UUID idLibro) {

        eliminarItemCarrito.ejecutar(idCarrito, idLibro);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Confirmar carrito", description = "Pasa el carrito de CREADO a PROCESANDO. No puede estar vacío.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrito confirmado"),
            @ApiResponse(responseCode = "400", description = "Carrito vacío o estado inválido", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "El carrito no existe", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable UUID idCarrito) {
        confirmarCarrito.ejecutar(idCarrito);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancelar la confirmación", description = "Devuelve el carrito de PROCESANDO a CREADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Confirmación cancelada"),
            @ApiResponse(responseCode = "400", description = "El carrito no está en estado PROCESANDO", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "El carrito no existe", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/cancelar-confirmacion")
    public ResponseEntity<Void> cancelarConfirmacion(@PathVariable UUID idCarrito) {
        cancelarConfirmacionCarrito.ejecutar(idCarrito);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Completar la venta", description = "Convierte el carrito confirmado en venta (estado VENDIDO) y descuenta el stock.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Venta completada"),
            @ApiResponse(responseCode = "400", description = "El carrito no está confirmado", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "El carrito no existe", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/completar-venta")
    public ResponseEntity<Void> completarVenta(@PathVariable UUID idCarrito) {
        completarVentaCarrito.ejecutar(idCarrito);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar carrito", description = "Eliminación lógica. Solo carritos en estado CREADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrito eliminado"),
            @ApiResponse(responseCode = "400", description = "El carrito no se puede eliminar en su estado actual", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "No es el dueño del carrito", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "El carrito no existe", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @DeleteMapping("/{idCarrito}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID idCarrito) {
        eliminarCarrito.ejecutar(idCarrito);
        return ResponseEntity.noContent().build();
    }

    private Map<String, String> titulosDe(Carrito carrito) {
        return obtenerTitulosCarritos.ejecutar(List.of(carrito));
    }
}