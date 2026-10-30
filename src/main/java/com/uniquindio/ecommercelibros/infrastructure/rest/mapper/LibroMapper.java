package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.libro.LibroResponse;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import org.springframework.stereotype.Component;
import com.uniquindio.ecommercelibros.aplication.dto.request.libro.RegistrarLibroRequest;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.*;

import java.math.BigDecimal;

@Component
public class LibroMapper {

    public LibroResponse toResponse(Libro libro) {
        return new LibroResponse(
                libro.getIdLibro(),
                libro.getTitulo().nombreLibro(),
                libro.getIdAutor(),
                libro.getIsbn().isbn(),
                libro.getDescripcion(),
                libro.getNumeroPaginas().numeroPaginas(),
                libro.getPrecio().monto(),
                libro.getPrecio().moneda(),
                libro.getStock().stock(),
                libro.getEdicion(),
                libro.getIdEditorial(),
                libro.getFechaPublicacion().fecha(),
                libro.getCategoria(),
                libro.getImagen(),
                libro.getEstado()
        );
    }


    public Libro toDomain(RegistrarLibroRequest request) {
        return Libro.crear(
                new NombreLibro(request.titulo()),
                request.idAutor(),
                new ISBN(request.isbn()),
                request.descripcion(),
                new NumeroPaginas(request.numeroPaginas()),
                new Precio(request.precio(), request.moneda()),
                new Stock(request.stock()),
                request.edicion(),
                request.idEditorial(),
                new FechaPublicacion(request.fechaPublicacion()),
                request.categoria(),
                request.imagen(),
                request.estado(),
                request.eliminado()
        );
    }

}
