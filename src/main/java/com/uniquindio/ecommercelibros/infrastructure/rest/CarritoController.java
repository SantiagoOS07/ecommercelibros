package com.uniquindio.ecommercelibros.infrastructure.rest;

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
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.BuscarLibroISBN;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/carritos")
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
    private final ConsultarLibroID consultarLibroID;
    private final BuscarLibroISBN buscarLibroISBN;
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
            ConsultarLibroID consultarLibroID,
            BuscarLibroISBN buscarLibroISBN,
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
        this.consultarLibroID = consultarLibroID;
        this.buscarLibroISBN = buscarLibroISBN;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CarritoResponse> crear(@AuthenticationPrincipal Jwt jwt) {

        Carrito carrito = registrarCarrito.ejecutar(jwt.getClaimAsString("uid"));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(carrito.getIdCarrito())
                .toUri();

        return ResponseEntity.created(location)
                .body(mapper.toResponse(carrito, Map.of()));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @GetMapping("/{idCarrito}")
    public ResponseEntity<CarritoResponse> consultarPorId(
            @PathVariable UUID idCarrito) {

        Carrito carrito = consultarCarritoPorID.ejecutar(idCarrito)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el carrito con ID: " + idCarrito
                ));

        return ResponseEntity.ok(mapper.toResponse(carrito, titulosDe(carrito)));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #idUsuario == authentication.principal.getClaimAsString('uid')")    @GetMapping(params = "idUsuario")
    public ResponseEntity<CarritoResponse> consultarPorUsuario(
            @RequestParam String idUsuario) {

        Carrito carrito = consultarCarritoPorUsuario.ejecutar(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El usuario " + idUsuario + " no tiene un carrito activo"
                ));

        return ResponseEntity.ok(mapper.toResponse(carrito, titulosDe(carrito)));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<CarritoResponse>> listar() {

        List<CarritoResponse> respuesta = listarCarritos.ejecutar()
                .stream()
                .map(carrito -> mapper.toResponse(carrito, titulosDe(carrito)))
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PostMapping("/{idCarrito}/items")
    public ResponseEntity<Void> agregarItem(
            @PathVariable UUID idCarrito,
            @Valid @RequestBody AgregarItemCarritoRequest request) {

        Libro libro = obtenerLibro(request.idLibro());
        libro.verificarDisponibleParaVenta();

        agregarItemCarrito.ejecutar(
                idCarrito,
                libro.getIsbn(),
                request.cantidad(),
                libro.getPrecio(),
                libro.getStock()
        );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/items/{idLibro}")
    public ResponseEntity<Void> actualizarCantidad(
            @PathVariable UUID idCarrito,
            @PathVariable UUID idLibro,
            @RequestParam int cantidad) {

        Libro libro = obtenerLibro(idLibro);

        actualizarCantidadItemCarrito.ejecutar(
                idCarrito,
                libro.getIsbn(),
                cantidad,
                libro.getStock()
        );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @DeleteMapping("/{idCarrito}/items/{idLibro}")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable UUID idCarrito,
            @PathVariable UUID idLibro) {

        Libro libro = obtenerLibro(idLibro);

        eliminarItemCarrito.ejecutar(
                idCarrito,
                libro.getIsbn()
        );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/confirmar")
    public ResponseEntity<Void> confirmar(
            @PathVariable UUID idCarrito) {

        confirmarCarrito.ejecutar(idCarrito);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/cancelar-confirmacion")
    public ResponseEntity<Void> cancelarConfirmacion(
            @PathVariable UUID idCarrito) {

        cancelarConfirmacionCarrito.ejecutar(idCarrito);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @PutMapping("/{idCarrito}/completar-venta")
    public ResponseEntity<Void> completarVenta(
            @PathVariable UUID idCarrito) {

        completarVentaCarrito.ejecutar(idCarrito);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR') or @seguridad.esDuenoDeCarrito(#idCarrito, authentication)")
    @DeleteMapping("/{idCarrito}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idCarrito) {

        eliminarCarrito.ejecutar(idCarrito);

        return ResponseEntity.noContent().build();
    }

    private Libro obtenerLibro(UUID idLibro) {
        return consultarLibroID.ConsultarLibroPorID(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con ID: " + idLibro
                ));
    }

    private Map<String, String> titulosDe(Carrito carrito) {
        Map<String, String> titulos = new HashMap<>();

        carrito.getItems().forEach(item -> {
            String isbn = item.getIsbnLibro().isbn();
            buscarLibroISBN.buscar(isbn).ifPresent(libro ->
                    titulos.put(isbn, libro.getTitulo().nombreLibro()));
        });

        return titulos;
    }
}
