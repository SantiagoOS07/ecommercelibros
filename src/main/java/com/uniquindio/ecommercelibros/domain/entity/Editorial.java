package com.uniquindio.ecommercelibros.domain.entity;

import java.util.Objects;
import java.util.UUID;

import com.uniquindio.ecommercelibros.domain.valueObject.Pais;


public class Editorial {

    private UUID idEditorial;
    private String nombreEditorial;
    private Pais pais;
    private String descripcionEditorial;

    public Editorial(UUID idEditorial, String nombreEditorial, Pais pais, String descripcionEditorial) {
        this.idEditorial = UUID.randomUUID();
        this.nombreEditorial = nombreEditorial;
        this.pais = pais;
        this.descripcionEditorial = descripcionEditorial;
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
