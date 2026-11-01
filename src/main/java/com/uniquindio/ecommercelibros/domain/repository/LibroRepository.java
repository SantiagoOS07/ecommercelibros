package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;

import java.util.*;

public interface LibroRepository {

    Optional<Libro> buscarPorISBN(String ISBN);
    Optional<Libro> buscarPorIdLibro(UUID idLibro);
    List<Libro> buscarPorNombre(String nombre);
    List<Libro> buscarPorAutor(String autor);
    void guardarLibro(Libro libro);
    boolean existePorIsbn(String isbn);
    Pagina<Libro> buscarLibros(FiltroLibros filtro, int pagina, int tamanio);


    default Pagina<Libro> buscarLibros(String palabraTitulo, EstadoLibro estado, int pagina, int tamanio) {
        return buscarLibros(FiltroLibros.porTituloYEstado(palabraTitulo, estado), pagina, tamanio);
    }
    Map<String, String> buscarTitulosPorIsbn(Collection<String> isbns);
    List<LibroVentas> buscarLibrosMasVendidos(int limite);
}