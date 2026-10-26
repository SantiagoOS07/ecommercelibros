package com.uniquindio.ecommercelibros.aplication.dto.response.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.Pagina;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;

import java.util.UUID;
import java.util.function.Function;

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
) {
    public static LibroDetalleResponse desde(Libro libro) {
        return new LibroDetalleResponse(
                libro.getIdLibro(),
                libro.getTitulo().nombreLibro(),
                libro.getIdAutor(),
                libro.getPrecio().monto(),
                libro.getPrecio().moneda(),
                libro.getCategoria(),
                libro.getImagen(),
                libro.getEstado()
                );
    }
}
