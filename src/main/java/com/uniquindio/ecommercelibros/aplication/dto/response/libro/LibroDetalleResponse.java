package com.uniquindio.ecommercelibros.aplication.dto.response.libro;

import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;

import java.util.UUID;

/** GET /libros y GET /libros?autor={nombreAutor} */
public record LibroDetalleResponse(
        UUID idLibro,
        String titulo,
        String idAutor,
        double precio,
        Moneda moneda,
        CategoriaLibro categoria,
        String imagen,
        EstadoLibro estado
) {}
