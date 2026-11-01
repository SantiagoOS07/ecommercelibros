package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;


public record FiltroLibros(
        String palabraTitulo,
        EstadoLibro estado,
        CategoriaLibro categoria,
        Double precioMinimo,
        Double precioMaximo,
        Moneda moneda
) {
    public FiltroLibros {
        if (precioMinimo != null && (precioMinimo.isNaN() || precioMinimo < 0)) {
            throw new ReglaDominioException("El precio mínimo no puede ser negativo");
        }
        if (precioMaximo != null && (precioMaximo.isNaN() || precioMaximo < 0)) {
            throw new ReglaDominioException("El precio máximo no puede ser negativo");
        }
        if (precioMinimo != null && precioMaximo != null && precioMinimo > precioMaximo) {
            throw new ReglaDominioException("El precio mínimo no puede ser mayor que el precio máximo");
        }
        if ((precioMinimo != null || precioMaximo != null) && moneda == null) {
            throw new ReglaDominioException("Para filtrar por precio debe indicar la moneda");
        }
    }

    public static FiltroLibros porTituloYEstado(String palabraTitulo, EstadoLibro estado) {
        return new FiltroLibros(palabraTitulo, estado, null, null, null, null);
    }
}