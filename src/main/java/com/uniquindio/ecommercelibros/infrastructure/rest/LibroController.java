package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.libro.*;
import com.uniquindio.ecommercelibros.aplication.dto.response.PaginaResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroDetalleResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.aplication.dto.response.libro.MasVendidoResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.*;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.FechaPublicacion;
import com.uniquindio.ecommercelibros.domain.valueObject.NombreLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.NumeroPaginas;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.LibroMapper;
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
    private final ListarLibrosMasVendidosUseCase listarLibrosMasVendidos;

    @GetMapping
    public PaginaResponse<LibroDetalleResponse> listarLibros(
            @RequestParam(name = "palabra", required = false) String palabra,
            @RequestParam(name = "estado", required = false) EstadoLibro estado,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            @RequestParam(name = "tamanio", defaultValue = "10") int tamanio) {
        return PaginaResponse.desde(listarLibros.ejecutar(palabra, estado, pagina, tamanio),
                LibroDetalleResponse::desde);
    }

    @GetMapping("/masVendidos")
    public List<MasVendidoResponse> listarLibrosMasVendidos(@RequestParam(name = "limite", defaultValue = "5") int limite) {
        return listarLibrosMasVendidos.ejecutar(limite).stream()
                .map(MasVendidoResponse::desde)
                .toList();
    }

    @GetMapping("/{idLibro}")
    public ResponseEntity<LibroResponse> consultarPorId(@PathVariable UUID idLibro) {
        Libro libro = consultarLibroID.ConsultarLibroPorID(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con ID: " + idLibro));

        return ResponseEntity.ok(mapper.toResponse(libro));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponse> buscarPorIsbn(@PathVariable String isbn) {
        Libro libro = buscarLibroISBN.buscar(isbn)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con ISBN: " + isbn));

        return ResponseEntity.ok(mapper.toResponse(libro));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<LibroResponse>> buscarPorNombre(@PathVariable String nombre) {
        List<LibroResponse> libros = buscarLibroNombre.buscar(nombre).stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(libros);
    }

    @GetMapping("/autor/{nombreAutor}")
    public ResponseEntity<List<LibroResponse>> buscarPorAutor(@PathVariable String nombreAutor) {
        List<LibroResponse> libros = buscarLibroAutor.buscar(nombreAutor).stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(libros);
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

    @PutMapping("/{idLibro}/titulo")
    public ResponseEntity<Void> actualizarTitulo(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarTituloLibroRequest request) {

        actualizarLibro.actualizarTitulo(idLibro, new NombreLibro(request.titulo()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/descripcion")
    public ResponseEntity<Void> actualizarDescripcion(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarDescripcionLibroRequest request) {

        actualizarLibro.actualizarDescripcion(idLibro, request.descripcion());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/numero-paginas")
    public ResponseEntity<Void> actualizarNumeroPaginas(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarNumeroPaginasLibroRequest request) {

        actualizarLibro.actualizarNumeroPaginas(idLibro, new NumeroPaginas(request.numeroPaginas()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/precio")
    public ResponseEntity<Void> actualizarPrecio(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarPrecioLibroRequest request) {

        actualizarLibro.actualizarPrecio(idLibro, new Precio(request.precio(), request.moneda()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/stock")
    public ResponseEntity<Void> actualizarStock(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarStockLibroRequest request) {

        actualizarLibro.actualizarStock(idLibro, new Stock(request.stock()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/edicion")
    public ResponseEntity<Void> actualizarEdicion(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarEdicionLibroRequest request) {

        actualizarLibro.actualizarEdicion(idLibro, request.edicion());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/editorial")
    public ResponseEntity<Void> actualizarEditorial(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarEditorialLibroRequest request) {

        actualizarLibro.actualizarEditorial(idLibro, request.idEditorial());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/fecha-publicacion")
    public ResponseEntity<Void> actualizarFechaPublicacion(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarFechaPublicacionLibroRequest request) {

        actualizarLibro.actualizarFechaPublicacion(idLibro, new FechaPublicacion(request.fechaPublicacion()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/categoria")
    public ResponseEntity<Void> actualizarCategoria(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarCategoriaLibroRequest request) {

        actualizarLibro.actualizarCategoria(idLibro, request.categoria());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/imagen")
    public ResponseEntity<Void> actualizarImagen(
            @PathVariable UUID idLibro,
            @Valid @RequestBody ActualizarImagenLibroRequest request) {

        actualizarLibro.actualizarImagen(idLibro, request.imagen());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/agotar")
    public ResponseEntity<Void> agotar(@PathVariable UUID idLibro) {
        actualizarLibro.actualizarEstado(idLibro, EstadoLibro.AGOTADO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable UUID idLibro) {
        actualizarLibro.actualizarEstado(idLibro, EstadoLibro.DISPONIBLE);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idLibro}/descontinuar")
    public ResponseEntity<Void> descontinuar(@PathVariable UUID idLibro) {
        actualizarLibro.actualizarEstado(idLibro, EstadoLibro.DESCONTINUADO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idLibro}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID idLibro) {
        eliminarLibro.eliminar(idLibro);
        return ResponseEntity.noContent().build();
    }
}