package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;

@Entity
@Table(name = "items_carrito")
@Getter
@Setter
@NoArgsConstructor
public class ItemCarritoEntity {

    @Id
    @Column(name = "id_item_carrito", nullable = false, length = 36)
    private String idItemCarrito;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_carrito", nullable = false)
    private CarritoEntity carrito;

    @Column(name = "isbn_libro", nullable = false, length = 13)
    private String isbnLibro;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "monto",
                    column = @Column(name = "precio_unitario_monto",
                            nullable = false, precision = 12, scale = 2)
            ),
            @AttributeOverride(
                    name = "moneda",
                    column = @Column(name = "precio_unitario_moneda",
                            nullable = false, length = 3)
            )
    })
    private PrecioEmbeddable precioUnitario;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "monto",
                    column = @Column(name = "precio_total_monto",
                            nullable = false, precision = 12, scale = 2)
            ),
            @AttributeOverride(
                    name = "moneda",
                    column = @Column(name = "precio_total_moneda",
                            nullable = false, length = 3)
            )
    })
    private PrecioEmbeddable precioTotal;


    @Column(name = "stock_libro", nullable = false)
    private Stock stockLibro;

    @Column(nullable = false)
    private int cantidad;


}
