package com.uniquindio.ecommercelibros.domain.entity;

import java.util.Objects;

public class Autor {

    private String idAutor;
    private String nombreAutor;
    private String biografia;
    private Nacionalidad nacionalidad;

    public Autor(String idAutor, String nombreAutor, String biografia, Nacionalidad nacionalidad) {
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.biografia = biografia;
        this.nacionalidad = nacionalidad;
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
