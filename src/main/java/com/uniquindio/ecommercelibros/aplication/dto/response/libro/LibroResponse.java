package com.uniquindio.ecommercelibros.aplication.dto.response.libro;

import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;

import java.time.LocalDate;
import java.util.UUID;

/** GET /libros/{idLibro}, GET /libros/isbn/{isbn}, GET /libros/nombre/{nombre} y POST /libros */
public record LibroResponse(
        UUID idLibro,
        String titulo,
        String idAutor,
        String isbn,
        String descripcion,
        int numeroPaginas,
        double precio,
        Moneda moneda,
        int stock,
        String edicion,
        UUID idEditorial,
        LocalDate fechaPublicacion,
        CategoriaLibro categoria,
        String imagen,
        EstadoLibro estado
) {}
