package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "editoriales")
@Getter
@Setter
@NoArgsConstructor
public class EditorialEntity {

    @Id
    @Column(name = "id_editorial", nullable = false, length = 36)
    private String idEditorial;

    @Column(name = "nombre_editorial", nullable = false, unique = true, length = 150)
    private String nombreEditorial;

    @Enumerated(EnumType.STRING)
    @Column(name = "pais_origen", nullable = false, length = 30)
    private Pais paisOrigen;

    @Column(name = "descripcion_editorial", length = 2000)
    private String descripcionEditorial;
}