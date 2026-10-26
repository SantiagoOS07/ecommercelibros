package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
public class AutorEntity {

    @Id
    @Column(name = "id_autor", nullable = false, length = 36)
    private String idAutor;

    @Column(name = "nombre_autor", nullable = false, length = 150)
    private String nombreAutor;

    @Column(length = 2000)
    private String biografia;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "autor_nacionalidades",
            joinColumns = @JoinColumn(name = "id_autor")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "nacionalidad", nullable = false, length = 30)
    private List<Nacionalidad> nacionalidades = new ArrayList<>();
}