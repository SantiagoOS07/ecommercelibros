package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PrecioEmbeddable {

    @Column(name = "precio_monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "precio_moneda", nullable = false, length = 3)
    private Moneda moneda;

    public PrecioEmbeddable(BigDecimal monto, Moneda moneda) {
        this.monto = monto;
        this.moneda = moneda;
    }

    public static PrecioEmbeddable desde(Precio precio) {
        return new PrecioEmbeddable(
                BigDecimal.valueOf(precio.monto()).setScale(2, RoundingMode.HALF_UP),
                precio.moneda()
        );
    }

    public Precio aDominio() {
        return new Precio(monto.doubleValue(), moneda);
    }
}