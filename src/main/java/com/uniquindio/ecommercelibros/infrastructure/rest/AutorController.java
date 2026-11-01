package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.autor.ActualizarBiografiaAutorRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.autor.ActualizarNombreAutorRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.autor.AgregarNacionalidadRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.autor.RegistrarAutorRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.autor.AutorResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ActualizarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ConsultarAutorID;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ConsultarAutorNombre;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.EliminarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ListarAutores;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.RegistrarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.BuscarLibroAutor;
import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.AutorMapper;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
@Tag(name = "Autores",
        description = "Autores del catálogo. Las consultas (GET) son públicas; registrar y modificar requieren ADMINISTRADOR o PROVEEDOR; "
                + "eliminar es solo del ADMINISTRADOR.")
public class AutorController {

    private final RegistrarAutor registrarAutor;
    private final ConsultarAutorID consultarAutorID;
    private final ConsultarAutorNombre consultarAutorNombre;
    private final ListarAutores listarAutores;
    private final ActualizarAutor actualizarAutor;
    private final EliminarAutor eliminarAutor;
    private final BuscarLibroAutor buscarLibroAutor;
    private final AutorMapper mapper;
    private final LibroMapper libroMapper;

    public AutorController(
            RegistrarAutor registrarAutor,
            ConsultarAutorID consultarAutorID,
            ConsultarAutorNombre consultarAutorNombre,
            ListarAutores listarAutores,
            ActualizarAutor actualizarAutor,
            EliminarAutor eliminarAutor,
            BuscarLibroAutor buscarLibroAutor,
            AutorMapper mapper,
            LibroMapper libroMapper) {

        this.registrarAutor = registrarAutor;
        this.consultarAutorID = consultarAutorID;
        this.consultarAutorNombre = consultarAutorNombre;
        this.listarAutores = listarAutores;
        this.actualizarAutor = actualizarAutor;
        this.eliminarAutor = eliminarAutor;
        this.buscarLibroAutor = buscarLibroAutor;
        this.mapper = mapper;
        this.libroMapper = libroMapper;
    }

    @Operation(summary = "Registrar autor", description = "ADMINISTRADOR o PROVEEDOR. El id no se puede repetir (ni siquiera el de un autor eliminado).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Autor creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un autor con ese id", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AutorResponse> registrar(@Valid @RequestBody RegistrarAutorRequest request) {

        Autor autor = registrarAutor.registrar(
                request.idAutor().trim(),
                request.nombreAutor().trim(),
                request.biografia(),
                request.nacionalidad()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(autor.getIdAutor())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toResponse(autor));
    }

    @Operation(summary = "Listar autores", description = "Ordenados por nombre. No incluye autores eliminados.")
    @ApiResponse(responseCode = "200", description = "Lista de autores")
    @GetMapping
    public ResponseEntity<List<AutorResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(listarAutores.listar()));
    }

    @Operation(summary = "Consultar autor por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "El autor no existe o fue eliminado", content = @Content)
    })
    @GetMapping("/{idAutor}")
    public ResponseEntity<AutorResponse> consultarPorId(@PathVariable String idAutor) {

        Autor autor = consultarAutorID.consultar(idAutor)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el autor con ID: " + idAutor
                ));

        return ResponseEntity.ok(mapper.toResponse(autor));
    }

    @Operation(summary = "Consultar autor por nombre", description = "Coincidencia exacta sin distinguir mayúsculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "No hay un autor con ese nombre", content = @Content)
    })
    @GetMapping("/nombre/{nombreAutor}")
    public ResponseEntity<AutorResponse> consultarPorNombre(@PathVariable String nombreAutor) {

        Autor autor = consultarAutorNombre.buscar(nombreAutor)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el autor con nombre: " + nombreAutor
                ));

        return ResponseEntity.ok(mapper.toResponse(autor));
    }

    @Operation(summary = "Libros de un autor", description = "Solo libros no eliminados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libros del autor (lista vacía si no tiene)"),
            @ApiResponse(responseCode = "404", description = "El autor no existe o fue eliminado", content = @Content)
    })
    @GetMapping("/{idAutor}/libros")
    public ResponseEntity<List<LibroResponse>> listarLibros(@PathVariable String idAutor) {
        List<LibroResponse> libros = buscarLibroAutor.buscarPorIdAutor(idAutor).stream()
                .map(libroMapper::toResponse)
                .toList();

        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Actualizar nombre", description = "ADMINISTRADOR o PROVEEDOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Nombre actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El autor no existe", content = @Content)
    })
    @PutMapping("/{idAutor}/nombre")
    public ResponseEntity<Void> actualizarNombre(
            @PathVariable String idAutor,
            @Valid @RequestBody ActualizarNombreAutorRequest request) {

        actualizarAutor.actualizarNombre(idAutor, request.nombreAutor());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar biografía", description = "ADMINISTRADOR o PROVEEDOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Biografía actualizada"),
            @ApiResponse(responseCode = "400", description = "Biografía demasiado larga", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El autor no existe", content = @Content)
    })
    @PutMapping("/{idAutor}/biografia")
    public ResponseEntity<Void> actualizarBiografia(
            @PathVariable String idAutor,
            @Valid @RequestBody ActualizarBiografiaAutorRequest request) {

        actualizarAutor.actualizarBiografia(idAutor, request.biografia());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar nacionalidad", description = "ADMINISTRADOR o PROVEEDOR.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Nacionalidad agregada"),
            @ApiResponse(responseCode = "400", description = "Nacionalidad inválida o repetida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El autor no existe", content = @Content)
    })
    @PostMapping("/{idAutor}/nacionalidades")
    public ResponseEntity<Void> agregarNacionalidad(
            @PathVariable String idAutor,
            @Valid @RequestBody AgregarNacionalidadRequest request) {

        actualizarAutor.agregarNacionalidad(idAutor, request.nacionalidad());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Quitar nacionalidad", description = "ADMINISTRADOR o PROVEEDOR. El autor debe conservar al menos una.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Nacionalidad eliminada"),
            @ApiResponse(responseCode = "400", description = "Nacionalidad inválida, no registrada o es la única", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El autor no existe", content = @Content)
    })
    @DeleteMapping("/{idAutor}/nacionalidades/{nacionalidad}")
    public ResponseEntity<Void> eliminarNacionalidad(
            @PathVariable String idAutor,
            @PathVariable Nacionalidad nacionalidad) {

        actualizarAutor.eliminarNacionalidad(idAutor, nacionalidad);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar autor", description = "Solo ADMINISTRADOR. Eliminación lógica; no se permite si el autor tiene libros no eliminados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor eliminado"),
            @ApiResponse(responseCode = "400", description = "El autor tiene libros registrados", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "No es ADMINISTRADOR", content = @Content),
            @ApiResponse(responseCode = "404", description = "El autor no existe o ya fue eliminado", content = @Content)
    })
    @DeleteMapping("/{idAutor}")
    public ResponseEntity<Void> eliminar(@PathVariable String idAutor) {

        eliminarAutor.eliminar(idAutor);
        return ResponseEntity.noContent().build();
    }
}