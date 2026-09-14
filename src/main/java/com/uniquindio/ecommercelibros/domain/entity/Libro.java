package com.uniquindio.ecommercelibros.domain.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class Libro {

    private final UUID idLibro;

    private String titulo;
    private String autor;
    private String isbn;
    private String descripcion;
    private int numeroPaginas;
    private double precio;
    private int stock;
    private String edicion;
    private String editorial;
    private LocalDate fechaPublicacion;
    private String categoria;
    private String imagen;
    private String estado;

    public Libro(String titulo, String autor, String isbn, String descripcion, int numeroPaginas, double precio, int stock,String edicion, String editorial, LocalDate fechaPublicacion, String categoria, String imagen, String estado) {
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

