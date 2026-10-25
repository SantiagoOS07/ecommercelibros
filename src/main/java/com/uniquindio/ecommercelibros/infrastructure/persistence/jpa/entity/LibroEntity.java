    package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

    import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
    import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;

    import jakarta.persistence.*;

    import java.time.LocalDate;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Entity
    @Table(name = "libros")
    @Getter
    @Setter
    @NoArgsConstructor
    public class LibroEntity {

        @Id
        @Column(name = "id_libro", nullable = false, length = 36)
        private String idLibro;

        @Column(nullable = false, length = 150)
        private String titulo;

        @Column(name = "id_autor", nullable = false, length = 36)
        private String idAutor;

        @Column(nullable = false, unique = true, length = 13)
        private String isbn;

        @Column(length = 2000)
        private String descripcion;

        @Column(name = "numero_paginas", nullable = false)
        private int numeroPaginas;

        @Embedded
        private PrecioEmbeddable precio;

        @Column(nullable = false)
        private int stock;

        @Column(nullable = false, length = 100)
        private String edicion;

        @Column(name = "id_editorial", nullable = false, length = 36)
        private String idEditorial;

        @Column(name = "fecha_publicacion", nullable = false)
        private LocalDate fechaPublicacion;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private CategoriaLibro categoria;

        @Column(length = 500)
        private String imagen;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private EstadoLibro estado;

        @Version
        private Long version;
    }

