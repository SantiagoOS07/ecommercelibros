package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.RegistrarLibroRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.RegistrarLibroUseCase;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final RegistrarLibroUseCase registrarLibro;
    private final ConsultarLibroID consultarLibroID;
    private final LibroMapper mapper;

    public LibroController(
            RegistrarLibroUseCase registrarLibroUseCase,
            ConsultarLibroID consultarLibroID,
            LibroMapper mapper) {
        this.registrarLibro = registrarLibroUseCase;
        this.consultarLibroID = consultarLibroID;
        this.mapper = mapper;
    }


    @PostMapping
    public ResponseEntity<LibroResponse> registrar(
            @Valid @RequestBody RegistrarLibroRequest request) {

        Libro libroRequest = mapper.toDomain(request);

        Libro libro = registrarLibro.registrarLibro(
                libroRequest.getTitulo(),
                libroRequest.getIdAutor(),
                libroRequest.getIsbn(),
                libroRequest.getDescripcion(),
                libroRequest.getNumeroPaginas(),
                libroRequest.getPrecio(),
                libroRequest.getStock(),
                libroRequest.getEdicion(),
                libroRequest.getIdEditorial(),
                libroRequest.getFechaPublicacion(),
                libroRequest.getCategoria(),
                libroRequest.getImagen(),
                libroRequest.getEstado()
        );

        LibroResponse response = mapper.toResponse(libro);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(libro.getIdLibro())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> consultarPorId(
            @PathVariable UUID id) {

        Libro libro = consultarLibroID
                .ConsultarLibroPorID(id)
                .orElseThrow(() -> new java.util.NoSuchElementException(
                        "No se encontró el libro con ID: " + id
                ));

        return ResponseEntity.ok(mapper.toResponse(libro));
    }
}

