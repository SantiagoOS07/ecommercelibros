package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PrecioEmbeddable {

    @Column(name = "precio_monto", nullable = false, precision = 12, scale = 2)
    private double monto;

    @Column(name = "precio_moneda", nullable = false, length = 3)
    private Moneda moneda;

    public PrecioEmbeddable(double monto, Moneda moneda) {
        this.monto = monto;
        this.moneda = moneda;
    }
}

