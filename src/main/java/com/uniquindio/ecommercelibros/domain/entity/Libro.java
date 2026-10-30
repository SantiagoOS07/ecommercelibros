package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
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
    private boolean eliminado;

    private Libro(UUID idLibro, NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado,  boolean eliminado) {
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
        this.eliminado = false;
    }

    public static Libro crear(NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado, boolean eliminado) {
        validar(titulo, idAutor, isbn, numeroPaginas, precio, stock,edicion, idEditorial, fechaPublicacion, categoria, estado);
        return new Libro(UUID.randomUUID(), titulo, idAutor, isbn, descripcion, numeroPaginas, precio, stock, edicion, idEditorial, fechaPublicacion, categoria, imagen, estado, eliminado);
    }

    public static Libro reconstruir(UUID idLibro, NombreLibro titulo, String idAutor, ISBN isbn, String descripcion, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, String imagen, EstadoLibro estado, boolean eliminado) {
        return new Libro(idLibro, titulo, idAutor, isbn, descripcion, numeroPaginas, precio, stock, edicion, idEditorial, fechaPublicacion, categoria, imagen, estado, eliminado);
    }

    private static void validar(NombreLibro titulo, String idAutor, ISBN isbn, NumeroPaginas numeroPaginas, Precio precio, Stock stock, String edicion, UUID idEditorial, FechaPublicacion fechaPublicacion, CategoriaLibro categoria, EstadoLibro estado) {
        if (titulo == null) {
            throw new CasillaVaciaException("El título del libro no puede ser nulo.");
        }
        if (idAutor == null || idAutor.isEmpty()) {
            throw new CasillaVaciaException("El id del autor no puede ser nulo o vacío.");
        }
        if (isbn == null) {
            throw new CasillaVaciaException("El ISBN del libro no puede ser nulo.");
        }
        if (numeroPaginas == null){
            throw new CasillaVaciaException("El numero de paginas no puede ser nulo.");
        }
        if (precio == null) {
            throw new CasillaVaciaException("El precio del libro no puede ser nulo.");
        }
        if (stock == null) {
            throw new CasillaVaciaException("El stock del libro no puede ser nulo.");
        }
        if (edicion == null) {
            throw new CasillaVaciaException("El edicion del libro no puede ser nulo.");
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
        verificarNoEliminado();
        if (nuevoTitulo == null) {
            throw new CasillaVaciaException("El título del libro no puede ser nulo.");
        }
        this.titulo = nuevoTitulo;
    }

    public void cambiarDescripcion(String nuevaDescripcion) {
        verificarNoEliminado();

        this.descripcion = nuevaDescripcion;
    }

    public void cambiarNumeroPaginas(NumeroPaginas nuevoNumeroPaginas) {
        verificarNoEliminado();

        if (nuevoNumeroPaginas == null) {
            throw new CasillaVaciaException("El número de páginas no puede ser nulo.");
        }
        this.numeroPaginas = nuevoNumeroPaginas;
    }

    public void cambiarPrecio(Precio nuevoPrecio) {
        verificarNoEliminado();

        if (nuevoPrecio == null) {
            throw new CasillaVaciaException("El precio del libro no puede ser nulo.");
        }
        this.precio = nuevoPrecio;
    }

    public void actualizarStock(Stock nuevoStock) {
        verificarNoEliminado();

        if (nuevoStock == null) {
            throw new CasillaVaciaException("El stock del libro no puede ser nulo.");
        }
        this.stock = nuevoStock;
    }

    public void cambiarEdicion(String nuevaEdicion) {
        verificarNoEliminado();

        this.edicion = nuevaEdicion;
    }

    public void cambiarEditorial(UUID nuevoIdEditorial) {
        verificarNoEliminado();

        if (nuevoIdEditorial == null) {
            throw new CasillaVaciaException("El id de la editorial no puede ser nulo.");
        }
        this.idEditorial = nuevoIdEditorial;
    }

    public void cambiarFechaPublicacion(FechaPublicacion nuevaFechaPublicacion) {
        verificarNoEliminado();

        if (nuevaFechaPublicacion == null) {
            throw new CasillaVaciaException("La fecha de publicación no puede ser nula.");
        }
        this.fechaPublicacion = nuevaFechaPublicacion;
    }

    public void cambiarCategoria(CategoriaLibro nuevaCategoria) {
        verificarNoEliminado();

        if (nuevaCategoria == null) {
            throw new CasillaVaciaException("La categoría del libro no puede ser nula.");
        }
        this.categoria = nuevaCategoria;
    }

    public void cambiarImagen(String nuevaImagen) {
        verificarNoEliminado();
        this.imagen = nuevaImagen;
    }

    public void cambiarEstado(EstadoLibro nuevoEstado) {
        verificarNoEliminado();

        if (nuevoEstado == null) {
            throw new CasillaVaciaException("El estado del libro no puede ser nulo.");
        }

        if (!this.estado.puedeTransicionarA(nuevoEstado)) {
            throw new ReglaDominioException("No se puede cambiar el estado");
        }

        this.estado = nuevoEstado;
    }

    public void eliminarLogicamente() {
        verificarNoEliminado();
        this.eliminado = true;
    }

    private void verificarNoEliminado() {
        if (eliminado) {
            throw new ReglaDominioException("El libro está eliminado y no puede modificarse.");
        }
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