package com.uniquindio.ecommercelibros.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idLibro;

    private String titulo;
    private String autor;
    private String isbn;
    private String descripcion;
    private int paginas;
    private double precio;
    private int stock;
    private String editorial;
    private LocalDate fechaPublicacion;
    private String categoria;
    private String imagen;
    private String estado;

    public Libro(String titulo, String autor, String isbn, String descripcion, int paginas, double precio, int stock, String editorial, LocalDate fechaPublicacion, String categoria, String imagen, String estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.descripcion = descripcion;
        this.paginas = paginas;
        this.precio = precio;
        this.stock = stock;
        this.editorial = editorial;
        this.fechaPublicacion = fechaPublicacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.estado = estado;
    }

    public Libro() {

    }
}
