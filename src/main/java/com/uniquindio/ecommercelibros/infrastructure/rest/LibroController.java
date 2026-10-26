package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.RegistrarLibroRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.PaginaResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroDetalleResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ListarLibros;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.RegistrarLibroUseCase;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final RegistrarLibroUseCase registrarLibro;
    private final ConsultarLibroID consultarLibroID;
    private final ListarLibros listarLibros;
    private final LibroMapper mapper;

    @GetMapping
    public PaginaResponse<LibroDetalleResponse> listarLibros(
            @RequestParam(name = "palabra", required = false) String palabra,
            @RequestParam(name = "estado", required = false) EstadoLibro estado,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            @RequestParam(name = "tamanio", defaultValue = "10") int tamanio) {
        return PaginaResponse.desde(listarLibros.ejecutar(palabra, estado, pagina, tamanio),
                LibroDetalleResponse::desde);
    }

    @PostMapping
    public ResponseEntity<LibroResponse> registrar(@Valid @RequestBody RegistrarLibroRequest request) {
        Libro libro = registrarLibro.registrarLibro(mapper.toDomain(request));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(libro.getIdLibro())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toResponse(libro));
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

