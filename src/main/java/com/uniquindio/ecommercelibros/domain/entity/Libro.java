package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class Libro {

    private final UUID idLibro;

    private NombreLibro titulo;
    private Autor autor;
    private ISBN isbn;
    private String descripcion;
    private NumeroPaginas numeroPaginas;
    private Precio precio;
    private Stock stock;
    private String edicion;
    private Editorial editorial;
    private FechaPublicacion fechaPublicacion;
    private CategoriaLibro categoria;
    private String imagen;
    private EstadoLibro estado;

    public Libro(NombreLibro titulo, Autor autor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, Editorial editorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado) {
        this.idLibro = UUID.randomUUID();
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.descripcion = descripcion;
        this.numeroPaginas = numeroPaginas;
        this.precio = precio;
        this.stock = stock;
        this.edicion = edicion;
        this.editorial = editorial;
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

