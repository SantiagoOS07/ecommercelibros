package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class Libro {

    private final UUID idLibro;

    private NombreLibro titulo;
    private String idAutor;
    private ISBN isbn;
    private String descripcion;
    private NumeroPaginas numeroPaginas;
    private Precio precio;
    private Stock stock;
    private String edicion;
    private UUID idEditorial;
    private FechaPublicacion fechaPublicacion;
    private CategoriaLibro categoria;
    private String imagen;
    private EstadoLibro estado;

    public Libro(NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado) {
        this.idLibro = UUID.randomUUID();
        this.titulo = titulo;
        this.idAutor = idAutor;
        this.isbn = isbn;
        this.descripcion = descripcion;
        this.numeroPaginas = numeroPaginas;
        this.precio = precio;
        this.stock = stock;
        this.edicion = edicion;
        this.idEditorial = idEditorial;
        this.fechaPublicacion = fechaPublicacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro libro)) return false;
        return idLibro.equals(libro.idLibro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLibro);
    }
}

