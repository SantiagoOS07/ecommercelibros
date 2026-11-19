package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Descuento;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class DescuentoEmbeddable {

    @Column(name = "descuento_porcentaje",precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "descuento_motivo", length = 100)
    private String motivo;

    @Column(name = "descuento_fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "descuento_fecha_fin")
    private LocalDate fechaFin;

    public DescuentoEmbeddable(BigDecimal porcentaje, String motivo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.porcentaje = porcentaje;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public static DescuentoEmbeddable desde(Descuento descuento) {
        return new DescuentoEmbeddable(
                BigDecimal.valueOf(descuento.porcentaje()),
                descuento.motivo(),
                descuento.fechaInicio(),
                descuento.fechaFin()
        );
    }

    public Descuento aDominio() {
        return new Descuento(porcentaje.doubleValue(), motivo, fechaInicio, fechaFin);
    }
}
