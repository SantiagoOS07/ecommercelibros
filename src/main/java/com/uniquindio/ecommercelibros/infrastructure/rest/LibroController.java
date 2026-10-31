package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.*;
import com.uniquindio.ecommercelibros.aplication.dto.response.PaginaResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroDetalleResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ActualizarLibro;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.BuscarLibroAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.BuscarLibroISBN;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.BuscarLibroNombre;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.EliminarLibro;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ListarLibros;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.RegistrarLibroUseCase;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.FechaPublicacion;
import com.uniquindio.ecommercelibros.domain.valueObject.NombreLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.NumeroPaginas;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@Tag(name = "Libros",
        description = "Catálogo de libros. Las consultas (GET) son públicas; registrar, actualizar y eliminar requieren rol ADMINISTRADOR o PROVEEDOR.")
public class LibroController {

    private final RegistrarLibroUseCase registrarLibro;
    private final ConsultarLibroID consultarLibroID;
    private final ListarLibros listarLibros;
    private final BuscarLibroISBN buscarLibroISBN;
    private final BuscarLibroNombre buscarLibroNombre;
    private final BuscarLibroAutor buscarLibroAutor;
    private final ActualizarLibro actualizarLibro;
    private final EliminarLibro eliminarLibro;
    private final LibroMapper mapper;

    @Operation(summary = "Listar libros (paginado)",
            description = "Filtra por palabra en el título y por estado (por defecto DISPONIBLE). Excluye los libros eliminados.")
    @ApiResponse(responseCode = "200", description = "Página de libros")
    @GetMapping
    public PaginaResponse<LibroDetalleResponse> listarLibros(
            @Parameter(description = "Palabra a buscar en el título") @RequestParam(name = "palabra", required = false) String palabra,
            @Parameter(description = "Estado del libro (por defecto DISPONIBLE)") @RequestParam(name = "estado", required = false) EstadoLibro estado,
            @Parameter(description = "Número de página, desde 0") @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            @Parameter(description = "Tamaño de página") @RequestParam(name = "tamanio", defaultValue = "10") int tamanio) {
        return PaginaResponse.desde(listarLibros.ejecutar(palabra, estado, pagina, tamanio),
                LibroDetalleResponse::desde);
    }

    @Operation(summary = "Consultar libro por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @GetMapping("/{idLibro}")
    public ResponseEntity<LibroResponse> consultarPorId(@PathVariable UUID idLibro) {
        Libro libro = consultarLibroID.ConsultarLibroPorID(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con ID: " + idLibro));

        return ResponseEntity.ok(mapper.toResponse(libro));
    }

    @Operation(summary = "Buscar libro por ISBN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "No hay un libro con ese ISBN", content = @Content)
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponse> buscarPorIsbn(@PathVariable String isbn) {
        Libro libro = buscarLibroISBN.buscar(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con ISBN: " + isbn));

        return ResponseEntity.ok(mapper.toResponse(libro));
    }

    @Operation(summary = "Buscar libros por nombre", description = "Coincidencia parcial, sin distinguir mayúsculas.")
    @ApiResponse(responseCode = "200", description = "Libros que coinciden (lista vacía si no hay)")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<LibroResponse>> buscarPorNombre(@PathVariable String nombre) {
        List<LibroResponse> libros = buscarLibroNombre.buscar(nombre).stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libros por autor")
    @ApiResponse(responseCode = "200", description = "Libros del autor (lista vacía si no hay)")
    @GetMapping("/autor/{nombreAutor}")
    public ResponseEntity<List<LibroResponse>> buscarPorAutor(@PathVariable String nombreAutor) {
        List<LibroResponse> libros = buscarLibroAutor.buscar(nombreAutor).stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Registrar libro", description = "Requiere ADMINISTRADOR o PROVEEDOR. El ISBN debe ser único (también frente a libros eliminados).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un libro con ese ISBN", content = @Content)
    })
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

    @Operation(summary = "Actualizar título")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Título actualizado"),
            @ApiResponse(responseCode = "400", description = "Título inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/titulo")
    public ResponseEntity<Void> actualizarTitulo(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarTituloLibroRequest request) {

        actualizarLibro.actualizarTitulo(idLibro, new NombreLibro(request.titulo()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar descripción")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Descripción actualizada"),
            @ApiResponse(responseCode = "400", description = "Descripción inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/descripcion")
    public ResponseEntity<Void> actualizarDescripcion(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarDescripcionLibroRequest request) {

        actualizarLibro.actualizarDescripcion(idLibro, request.descripcion());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar número de páginas")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Número de páginas actualizado"),
            @ApiResponse(responseCode = "400", description = "Valor inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/numero-paginas")
    public ResponseEntity<Void> actualizarNumeroPaginas(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarNumeroPaginasLibroRequest request) {

        actualizarLibro.actualizarNumeroPaginas(idLibro, new NumeroPaginas(request.numeroPaginas()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar precio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Precio actualizado"),
            @ApiResponse(responseCode = "400", description = "Precio o moneda inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/precio")
    public ResponseEntity<Void> actualizarPrecio(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarPrecioLibroRequest request) {

        actualizarLibro.actualizarPrecio(idLibro, new Precio(request.precio(), request.moneda()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar stock")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Stock actualizado"),
            @ApiResponse(responseCode = "400", description = "Stock inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/stock")
    public ResponseEntity<Void> actualizarStock(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarStockLibroRequest request) {

        actualizarLibro.actualizarStock(idLibro, new Stock(request.stock()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar edición")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Edición actualizada"),
            @ApiResponse(responseCode = "400", description = "Edición inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/edicion")
    public ResponseEntity<Void> actualizarEdicion(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarEdicionLibroRequest request) {

        actualizarLibro.actualizarEdicion(idLibro, request.edicion());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar editorial")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Editorial actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/editorial")
    public ResponseEntity<Void> actualizarEditorial(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarEditorialLibroRequest request) {

        actualizarLibro.actualizarEditorial(idLibro, request.idEditorial());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar fecha de publicación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fecha actualizada"),
            @ApiResponse(responseCode = "400", description = "Fecha inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/fecha-publicacion")
    public ResponseEntity<Void> actualizarFechaPublicacion(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarFechaPublicacionLibroRequest request) {

        actualizarLibro.actualizarFechaPublicacion(idLibro, new FechaPublicacion(request.fechaPublicacion()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría actualizada"),
            @ApiResponse(responseCode = "400", description = "Categoría inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/categoria")
    public ResponseEntity<Void> actualizarCategoria(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarCategoriaLibroRequest request) {

        actualizarLibro.actualizarCategoria(idLibro, request.categoria());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar imagen")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imagen actualizada"),
            @ApiResponse(responseCode = "400", description = "Imagen inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/imagen")
    public ResponseEntity<Void> actualizarImagen(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarImagenLibroRequest request) {

        actualizarLibro.actualizarImagen(idLibro, request.imagen());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marcar libro como AGOTADO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado actualizado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/agotar")
    public ResponseEntity<Void> agotar(@PathVariable UUID idLibro) {
        actualizarLibro.actualizarEstado(idLibro, EstadoLibro.AGOTADO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reactivar libro (DISPONIBLE)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado actualizado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable UUID idLibro) {
        actualizarLibro.actualizarEstado(idLibro, EstadoLibro.DISPONIBLE);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Descontinuar libro")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado actualizado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @PutMapping("/{idLibro}/descontinuar")
    public ResponseEntity<Void> descontinuar(@PathVariable UUID idLibro) {
        actualizarLibro.actualizarEstado(idLibro, EstadoLibro.DESCONTINUADO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar libro", description = "Eliminación lógica: deja de listarse y consultarse, pero su ISBN sigue ocupado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El libro no existe", content = @Content)
    })
    @DeleteMapping("/{idLibro}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID idLibro) {
        eliminarLibro.eliminar(idLibro);
        return ResponseEntity.noContent().build();
    }
}