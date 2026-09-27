package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;

public class ActualizarAutor {

    private final AutorRepository autorRepository;

    public ActualizarAutor(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void actualizarNombre(String idAutor, String nuevoNombre) {
        Autor autor = obtenerAutor(idAutor);

        autor.cambiarNombre(nuevoNombre);

        autorRepository.guardarAutor(autor);
    }

    public void actualizarBiografia(String idAutor, String nuevaBiografia) {
        Autor autor = obtenerAutor(idAutor);

        autor.cambiarBiografia(nuevaBiografia);

        autorRepository.guardarAutor(autor);
    }

    public void agregarNacionalidad(String idAutor, Nacionalidad nacionalidad) {
        Autor autor = obtenerAutor(idAutor);

        autor.agregarNacionalidad(nacionalidad);

        autorRepository.guardarAutor(autor);
    }

    public void eliminarNacionalidad(String idAutor, Nacionalidad nacionalidad) {
        Autor autor = obtenerAutor(idAutor);

        autor.eliminarNacionalidad(nacionalidad);

        autorRepository.guardarAutor(autor);
    }

    private Autor obtenerAutor(String idAutor) {
        return autorRepository.buscarPorIdAutor(idAutor)
                .orElseThrow(() ->
                        new IllegalArgumentException("El autor no existe"));
    }
}
