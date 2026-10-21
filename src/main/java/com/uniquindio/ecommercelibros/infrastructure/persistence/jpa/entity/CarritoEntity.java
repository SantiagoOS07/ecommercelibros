package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoCarrito;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
public class CarritoEntity {

    @Id
    @Column(name = "id_carrito", nullable = false, length = 36)
    private String idCarrito;

    @Column(name = "id_usuario", nullable = false, length = 36)
    private String idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoCarrito estado;

    @OneToMany(
            mappedBy = "carrito",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemCarritoEntity> items = new ArrayList<>();
}

