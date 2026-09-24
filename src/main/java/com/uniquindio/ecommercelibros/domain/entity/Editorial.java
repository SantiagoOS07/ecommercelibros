package com.uniquindio.ecommercelibros.domain.entity;

import java.util.Objects;
import java.util.UUID;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import lombok.Getter;

@Getter
public class Editorial {

    private final UUID idEditorial;
    private String nombreEditorial;
    private final Pais paisOrigen;
    private String descripcionEditorial; // La descripción de la editorial puede ser nula o vacia

    private Editorial(String nombreEditorial, Pais paisOrigen, String descripcionEditorial) {
        this.idEditorial = UUID.randomUUID();
        this.nombreEditorial = nombreEditorial;
        this.paisOrigen = paisOrigen;
        this.descripcionEditorial = descripcionEditorial;
    }

    public static Editorial crear(String nombreEditorial, Pais paisOrigen, String descripcionEditorial) {
        validar(nombreEditorial, paisOrigen, descripcionEditorial);
        return new Editorial(nombreEditorial, paisOrigen, descripcionEditorial);
    }

    private static void validar(String nombreEditorial, Pais paisOrigen, String descripcionEditorial) {
        if (nombreEditorial == null || nombreEditorial.isEmpty()) {
            throw new ReglaDominioException("El nombre de la editorial no puede ser nulo o vacío");
        }
        if (paisOrigen == null) {
            throw new ReglaDominioException("El país de origen de la editorial no puede ser nulo");
        }
        if (descripcionEditorial == null || descripcionEditorial.isEmpty()) {
            throw new ReglaDominioException("La descripción de la editorial no puede ser nula o vacía");
        }
    }

    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new ReglaDominioException("El nuevo nombre de la editorial no puede ser nulo o vacío");
        }
        this.nombreEditorial = nuevoNombre;
    }

    public void cambiarDescripcion(String nuevaDescripcion) {
        this.descripcionEditorial = nuevaDescripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Editorial editorial = (Editorial) o;
        return Objects.equals(idEditorial, editorial.idEditorial);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idEditorial);
    }
}
