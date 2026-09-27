package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;

import java.util.UUID;

public class ActualizarLibro {

    private final LibroRepository libroRepository;

    public ActualizarLibro(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void actualizarTitulo(UUID idLibro, NombreLibro nuevoTitulo) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarTitulo(nuevoTitulo);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarDescripcion(UUID idLibro, String nuevaDescripcion) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarDescripcion(nuevaDescripcion);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarNumeroPaginas(UUID idLibro, NumeroPaginas nuevoNumeroPaginas) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarNumeroPaginas(nuevoNumeroPaginas);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarPrecio(UUID idLibro, Precio nuevoPrecio) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarPrecio(nuevoPrecio);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarStock(UUID idLibro, Stock nuevoStock) {
        Libro libro = obtenerLibro(idLibro);
        libro.actualizarStock(nuevoStock);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarEdicion(UUID idLibro, String nuevaEdicion) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarEdicion(nuevaEdicion);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarEditorial(UUID idLibro, UUID nuevoIdEditorial) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarEditorial(nuevoIdEditorial);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarFechaPublicacion(UUID idLibro, FechaPublicacion nuevaFechaPublicacion) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarFechaPublicacion(nuevaFechaPublicacion);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarCategoria(UUID idLibro, CategoriaLibro nuevaCategoria) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarCategoria(nuevaCategoria);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarImagen(UUID idLibro, String nuevaImagen) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarImagen(nuevaImagen);
        libroRepository.guardarLibro(libro);
    }

    public void actualizarEstado(UUID idLibro, EstadoLibro nuevoEstado) {
        Libro libro = obtenerLibro(idLibro);
        libro.cambiarEstado(nuevoEstado);
        libroRepository.guardarLibro(libro);
    }

    private Libro obtenerLibro(UUID idLibro) {
        return libroRepository.buscarPorIdLibro(idLibro)
                .orElseThrow(() ->
                        new IllegalArgumentException("El libro no existe"));
    }
}
