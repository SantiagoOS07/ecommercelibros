package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.autor.ActualizarBiografiaAutorRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.autor.ActualizarNombreAutorRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.autor.AgregarNacionalidadRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.autor.RegistrarAutorRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.autor.AutorResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ActualizarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ConsultarAutorID;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ConsultarAutorNombre;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.EliminarAutor;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.ListarAutores;
import com.uniquindio.ecommercelibros.aplication.usecase.autor.RegistrarAutor;
import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.AutorMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final RegistrarAutor registrarAutor;
    private final ConsultarAutorID consultarAutorID;
    private final ConsultarAutorNombre consultarAutorNombre;
    private final ListarAutores listarAutores;
    private final ActualizarAutor actualizarAutor;
    private final EliminarAutor eliminarAutor;
    private final AutorMapper mapper;

    public AutorController(
            RegistrarAutor registrarAutor,
            ConsultarAutorID consultarAutorID,
            ConsultarAutorNombre consultarAutorNombre,
            ListarAutores listarAutores,
            ActualizarAutor actualizarAutor,
            EliminarAutor eliminarAutor,
            AutorMapper mapper) {

        this.registrarAutor = registrarAutor;
        this.consultarAutorID = consultarAutorID;
        this.consultarAutorNombre = consultarAutorNombre;
        this.listarAutores = listarAutores;
        this.actualizarAutor = actualizarAutor;
        this.eliminarAutor = eliminarAutor;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AutorResponse> registrar(@Valid @RequestBody RegistrarAutorRequest request) {

        Autor autor = registrarAutor.registrar(
                request.idAutor(),
                request.nombreAutor(),
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

    @GetMapping
    public ResponseEntity<List<AutorResponse>> listar() {
        return ResponseEntity.ok(mapper.toResponseList(listarAutores.listar()));
    }

    @GetMapping("/{idAutor}")
    public ResponseEntity<AutorResponse> consultarPorId(@PathVariable String idAutor) {

        Autor autor = consultarAutorID.consultar(idAutor)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el autor con ID: " + idAutor
                ));

        return ResponseEntity.ok(mapper.toResponse(autor));
    }

    @GetMapping("/nombre/{nombreAutor}")
    public ResponseEntity<AutorResponse> consultarPorNombre(@PathVariable String nombreAutor) {

        Autor autor = consultarAutorNombre.buscar(nombreAutor)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el autor con nombre: " + nombreAutor
                ));

        return ResponseEntity.ok(mapper.toResponse(autor));
    }

    @PutMapping("/{idAutor}/nombre")
    public ResponseEntity<Void> actualizarNombre(
            @PathVariable String idAutor,
            @Valid @RequestBody ActualizarNombreAutorRequest request) {

        actualizarAutor.actualizarNombre(idAutor, request.nombreAutor());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idAutor}/biografia")
    public ResponseEntity<Void> actualizarBiografia(
            @PathVariable String idAutor,
            @Valid @RequestBody ActualizarBiografiaAutorRequest request) {

        actualizarAutor.actualizarBiografia(idAutor, request.biografia());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idAutor}/nacionalidades")
    public ResponseEntity<Void> agregarNacionalidad(
            @PathVariable String idAutor,
            @Valid @RequestBody AgregarNacionalidadRequest request) {

        actualizarAutor.agregarNacionalidad(idAutor, request.nacionalidad());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idAutor}/nacionalidades/{nacionalidad}")
    public ResponseEntity<Void> eliminarNacionalidad(
            @PathVariable String idAutor,
            @PathVariable Nacionalidad nacionalidad) {

        actualizarAutor.eliminarNacionalidad(idAutor, nacionalidad);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idAutor}")
    public ResponseEntity<Void> eliminar(@PathVariable String idAutor) {

        eliminarAutor.eliminar(idAutor);
        return ResponseEntity.noContent().build();
    }
}