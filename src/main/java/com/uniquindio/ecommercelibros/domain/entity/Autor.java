package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Autor {

    private final String idAutor;
    private String nombreAutor;
    private String biografia; // La biografia del autor puede ser nula o vacia
    private final List<Nacionalidad> nacionalidades;

    private Autor(String idAutor, String nombreAutor, String biografia, Nacionalidad nacionalidad) {
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.biografia = biografia;
        this.nacionalidades = new ArrayList<>();
        this.nacionalidades.add(nacionalidad);
    }

    public static Autor crear(String idAutor, String nombreAutor, String biografia, Nacionalidad nacionalidad) {
        validar(idAutor, nombreAutor, nacionalidad);
        return new Autor(idAutor, nombreAutor, biografia, nacionalidad);
    }

    private static void validar(String idAutor, String nombreAutor, Nacionalidad nacionalidad) {
        if (idAutor == null || idAutor.isEmpty()) {
            throw new ReglaDominioException("El id del autor no puede ser nulo o vacío.");
        }
        if (nombreAutor == null || nombreAutor.isEmpty()) {
            throw new ReglaDominioException("El nombre del autor no puede ser nulo o vacío.");
        }
        if (nacionalidad == null) {
            throw new ReglaDominioException("La nacionalidad del autor no puede ser nula.");
        }
    }

    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new ReglaDominioException("El nuevo nombre del autor no puede ser nulo o vacío.");
        }
        this.nombreAutor = nuevoNombre;
    }

    public void cambiarBiografia(String nuevaBiografia) {
        this.biografia = nuevaBiografia;
    }

    public void agregarNacionalidad(Nacionalidad nuevaNacionalidad) {
        if (this.nacionalidades.contains(nuevaNacionalidad)) {
            throw new ReglaDominioException("La nacionalidad del autor no se puede repetir.");
        }
        this.nacionalidades.add(nuevaNacionalidad);
    }

    public void eliminarNacionalidad(Nacionalidad nuevaNacionalidad) {
        if (!(this.nacionalidades.contains(nuevaNacionalidad))) {
            throw new ReglaDominioException("La nacionalidad seleccionada no se puede eliminar.");
        }
        this.nacionalidades.remove(nuevaNacionalidad);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(idAutor, autor.idAutor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idAutor);
    }
}
