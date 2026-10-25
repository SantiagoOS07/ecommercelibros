package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.FechaPublicacion;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.NombreLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.NumeroPaginas;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.LibroEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.PrecioEmbeddable;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LibroPersistenceMapper {

    public LibroEntity toEntity(Libro libro) {
        LibroEntity entity = new LibroEntity();
        entity.setIdLibro(libro.getIdLibro().toString());
        return actualizarEntity(entity, libro);
    }

    public LibroEntity actualizarEntity(LibroEntity entity, Libro libro) {
        entity.setTitulo(libro.getTitulo().nombreLibro());
        entity.setIdAutor(libro.getIdAutor());
        entity.setIsbn(libro.getIsbn().isbn());
        entity.setDescripcion(libro.getDescripcion());
        entity.setNumeroPaginas(libro.getNumeroPaginas().numeroPaginas());
        entity.setPrecio(PrecioEmbeddable.desde(libro.getPrecio()));
        entity.setStock(libro.getStock().stock());
        entity.setEdicion(libro.getEdicion());
        entity.setIdEditorial(libro.getIdEditorial().toString());
        entity.setFechaPublicacion(libro.getFechaPublicacion().fecha());
        entity.setCategoria(libro.getCategoria());
        entity.setImagen(libro.getImagen());
        entity.setEstado(libro.getEstado());
        return entity;
    }

    public Libro toDomain(LibroEntity entity) {

        return Libro.reconstruir(
                UUID.fromString(entity.getIdLibro()),
                new NombreLibro(entity.getTitulo()),
                entity.getIdAutor(),
                new ISBN(entity.getIsbn()),
                entity.getDescripcion(),
                new NumeroPaginas(entity.getNumeroPaginas()),
                entity.getPrecio().aDominio(),
                new Stock(entity.getStock()),
                entity.getEdicion(),
                UUID.fromString(entity.getIdEditorial()),
                new FechaPublicacion(entity.getFechaPublicacion()),
                entity.getCategoria(),
                entity.getImagen(),
                entity.getEstado()
        );
    }
}

