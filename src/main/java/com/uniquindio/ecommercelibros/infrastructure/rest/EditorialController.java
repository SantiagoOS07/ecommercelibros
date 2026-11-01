package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.editorial.RegistrarEditorialRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.editorial.EditorialResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.editorial.ConsultarEditorialID;
import com.uniquindio.ecommercelibros.aplication.usecase.editorial.ListarEditoriales;
import com.uniquindio.ecommercelibros.aplication.usecase.editorial.RegistrarEditorial;
import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.EditorialMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/editoriales")
@Tag(name = "Editoriales",
        description = "Editoriales a las que pertenecen los libros (idEditorial del libro). "
                + "Consultar requiere estar autenticado; registrar requiere ADMINISTRADOR o PROVEEDOR.")
public class EditorialController {

    private final RegistrarEditorial registrarEditorial;
    private final ConsultarEditorialID consultarEditorialID;
    private final ListarEditoriales listarEditoriales;
    private final EditorialMapper mapper;

    public EditorialController(RegistrarEditorial registrarEditorial,
                               ConsultarEditorialID consultarEditorialID,
                               ListarEditoriales listarEditoriales,
                               EditorialMapper mapper) {
        this.registrarEditorial = registrarEditorial;
        this.consultarEditorialID = consultarEditorialID;
        this.listarEditoriales = listarEditoriales;
        this.mapper = mapper;
    }

    @Operation(summary = "Registrar editorial", description = "ADMINISTRADOR o PROVEEDOR. El nombre no se puede repetir.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Editorial creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe una editorial con ese nombre", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'PROVEEDOR')")
    @PostMapping
    public ResponseEntity<EditorialResponse> registrar(@Valid @RequestBody RegistrarEditorialRequest request) {
        Editorial editorial = registrarEditorial.registrar(
                request.nombreEditorial().trim(),
                request.paisOrigen(),
                request.descripcionEditorial());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(editorial.getIdEditorial())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toResponse(editorial));
    }

    @Operation(summary = "Listar editoriales", description = "Ordenadas por nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de editoriales"),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EditorialResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(listarEditoriales.listar()));
    }

    @Operation(summary = "Consultar editorial por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Editorial encontrada"),
            @ApiResponse(responseCode = "400", description = "El id no es un UUID válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sin token", content = @Content),
            @ApiResponse(responseCode = "404", description = "La editorial no existe", content = @Content)
    })
    @GetMapping("/{idEditorial}")
    public ResponseEntity<EditorialResponse> consultarPorId(@PathVariable UUID idEditorial) {
        return ResponseEntity.ok(mapper.toResponse(consultarEditorialID.consultar(idEditorial)));
    }
}