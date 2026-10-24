package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items_venta")
@Getter
@Setter
@NoArgsConstructor
public class ItemVentaEntity {
    @Id
    @Column(name = "id_item_venta", nullable = false)
    private String idItemVenta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_venta", nullable = false)
    private VentaEntity venta;

    @Embedded
    private ISBNEmbeddable isbn;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "monto",
                    column = @Column(name = "precio_unitario_monto", nullable = false, precision = 12, scale = 2)),
            @AttributeOverride(name = "moneda",
                    column = @Column(name = "precio_unitario_moneda", nullable = false, length = 3))
    })
    private PrecioEmbeddable precioUnitario;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "monto",
                    column = @Column(name = "precio_total_monto", nullable = false, precision = 12, scale = 2)),
            @AttributeOverride(name = "moneda",
                    column = @Column(name = "precio_total_moneda", nullable = false, length = 3))
    })
    private PrecioEmbeddable precioTotal;
}
