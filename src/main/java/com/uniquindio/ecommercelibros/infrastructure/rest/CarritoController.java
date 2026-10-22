package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.AgregarItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.AgregarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ActualizarCantidadItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.CompletarVentaCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final RegistrarCarrito registrarCarrito;
    private final AgregarItemCarrito agregarItemCarrito;
    private final ActualizarCantidadItemCarrito actualizarCantidadItemCarrito;
    private final EliminarItemCarrito eliminarItemCarrito;
    private final CompletarVentaCarrito completarVentaCarrito;
    private final ConsultarLibroID consultarLibroID;
    private final CarritoMapper mapper;

    public CarritoController(
            RegistrarCarrito registrarCarrito,
            AgregarItemCarrito agregarItemCarrito,
            ActualizarCantidadItemCarrito actualizarCantidadItemCarrito,
            EliminarItemCarrito eliminarItemCarrito,
            CompletarVentaCarrito completarVentaCarrito,
            ConsultarLibroID consultarLibroID,
            CarritoMapper mapper) {

        this.registrarCarrito = registrarCarrito;
        this.agregarItemCarrito = agregarItemCarrito;
        this.actualizarCantidadItemCarrito = actualizarCantidadItemCarrito;
        this.eliminarItemCarrito = eliminarItemCarrito;
        this.completarVentaCarrito = completarVentaCarrito;
        this.consultarLibroID = consultarLibroID;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestParam String idUsuario) {

        Carrito carrito = registrarCarrito.ejecutar(idUsuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(carrito.getIdCarrito())
                .toUri();

        return ResponseEntity.created(location)
                .body(mapper.toResponse(carrito, Map.of()));
    }

    @PostMapping("/{idCarrito}/items")
    public ResponseEntity<Void> agregarItem(
            @PathVariable UUID idCarrito,
            @Valid @RequestBody AgregarItemCarritoRequest request) {

        Optional<Libro> libro = consultarLibroID.ConsultarLibroPorID(request.idLibro());

        agregarItemCarrito.ejecutar(
                idCarrito,
                libro.get().getIsbn(),
                request.cantidad(),
                libro.get().getPrecio(),
                libro.get().getStock()
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idCarrito}/items/{idLibro}")
    public ResponseEntity<Void> actualizarCantidad(
            @PathVariable UUID idCarrito,
            @PathVariable UUID idLibro,
            @RequestParam int cantidad) {

        Optional<Libro> libro = consultarLibroID.ConsultarLibroPorID(idLibro);

        actualizarCantidadItemCarrito.ejecutar(
                idCarrito,
                libro.get().getIsbn(),
                cantidad,
                libro.get().getStock()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idCarrito}/items/{idLibro}")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable UUID idCarrito,
            @PathVariable UUID idLibro) {

        Optional<Libro> libro = consultarLibroID.ConsultarLibroPorID(idLibro);

        eliminarItemCarrito.ejecutar(
                idCarrito,
                libro.get().getIsbn()
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idCarrito}/completar-venta")
    public ResponseEntity<Void> completarVenta(
            @PathVariable UUID idCarrito) {

        completarVentaCarrito.ejecutar(idCarrito);

        return ResponseEntity.noContent().build();
    }
}
