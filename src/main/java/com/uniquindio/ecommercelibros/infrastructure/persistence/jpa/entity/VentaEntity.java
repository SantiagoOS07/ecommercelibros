package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import  com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
public class VentaEntity {
    @Id
    @Column(name = "id_venta", nullable = false)
    private String idVenta;

    @Column(name = "id_usuario", nullable = false)
    private String idUsuario;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Embedded
    private PrecioEmbeddable precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_venta", nullable = false)
    private EstadoPedido estadoVenta;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVentaEntity> items = new ArrayList<>();
}
