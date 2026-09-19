package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
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

    private Libro(UUID idLibro, NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado) {
        this.idLibro = idLibro;
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

    public static Libro crear(NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado) {
        validar(titulo, idAutor, isbn, precio, stock, idEditorial, fechaPublicacion, categoria, estado);
        return new Libro(UUID.randomUUID(), titulo, idAutor, isbn, descripcion, numeroPaginas, precio, stock, edicion, idEditorial, fechaPublicacion, categoria, imagen, estado);
    }

    private static void validar(NombreLibro titulo, String idAutor, ISBN isbn, Precio precio, Stock stock, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, EstadoLibro estado) {
        if (titulo == null) {
            throw new CasillaVaciaException("El título del libro no puede ser nulo.");
        }
        if (idAutor == null || idAutor.isEmpty()) {
            throw new CasillaVaciaException("El id del autor no puede ser nulo o vacío.");
        }
        if (isbn == null) {
            throw new CasillaVaciaException("El ISBN del libro no puede ser nulo.");
        }
        if (precio == null) {
            throw new CasillaVaciaException("El precio del libro no puede ser nulo.");
        }
        if (stock == null) {
            throw new CasillaVaciaException("El stock del libro no puede ser nulo.");
        }
        if (idEditorial == null) {
            throw new CasillaVaciaException("El id de la editorial no puede ser nulo.");
        }
        if (fechaPublicacion == null) {
            throw new CasillaVaciaException("La fecha de publicación no puede ser nula.");
        }
        if (categoria == null) {
            throw new CasillaVaciaException("La categoría del libro no puede ser nula.");
        }
        if (estado == null) {
            throw new CasillaVaciaException("El estado del libro no puede ser nulo.");
        }
    }

    public void cambiarTitulo(NombreLibro nuevoTitulo) {
        if (nuevoTitulo == null) {
            throw new CasillaVaciaException("El título del libro no puede ser nulo.");
        }
        this.titulo = nuevoTitulo;
    }

    public void cambiarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    public void cambiarNumeroPaginas(NumeroPaginas nuevoNumeroPaginas) {
        if (nuevoNumeroPaginas == null) {
            throw new CasillaVaciaException("El número de páginas no puede ser nulo.");
        }
        this.numeroPaginas = nuevoNumeroPaginas;
    }

    public void cambiarPrecio(Precio nuevoPrecio) {
        if (nuevoPrecio == null) {
            throw new CasillaVaciaException("El precio del libro no puede ser nulo.");
        }
        this.precio = nuevoPrecio;
    }

    public void actualizarStock(Stock nuevoStock) {
        if (nuevoStock == null) {
            throw new CasillaVaciaException("El stock del libro no puede ser nulo.");
        }
        this.stock = nuevoStock;
    }

    public void cambiarEdicion(String nuevaEdicion) {
        this.edicion = nuevaEdicion;
    }

    public void cambiarEditorial(UUID nuevoIdEditorial) {
        if (nuevoIdEditorial == null) {
            throw new CasillaVaciaException("El id de la editorial no puede ser nulo.");
        }
        this.idEditorial = nuevoIdEditorial;
    }

    public void cambiarFechaPublicacion(FechaPublicacion nuevaFechaPublicacion) {
        if (nuevaFechaPublicacion == null) {
            throw new CasillaVaciaException("La fecha de publicación no puede ser nula.");
        }
        this.fechaPublicacion = nuevaFechaPublicacion;
    }

    public void cambiarCategoria(CategoriaLibro nuevaCategoria) {
        if (nuevaCategoria == null) {
            throw new CasillaVaciaException("La categoría del libro no puede ser nula.");
        }
        this.categoria = nuevaCategoria;
    }

    public void cambiarImagen(String nuevaImagen) {
        this.imagen = nuevaImagen;
    }

    public void cambiarEstado(EstadoLibro nuevoEstado) {
        if (nuevoEstado == null) {
            throw new CasillaVaciaException("El estado del libro no puede ser nulo.");
        }
        this.estado = nuevoEstado;
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