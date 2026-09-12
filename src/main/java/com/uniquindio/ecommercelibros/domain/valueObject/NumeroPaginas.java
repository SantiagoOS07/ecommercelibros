package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;

public record NumeroPaginas(int numeroPaginas) {
    public NumeroPaginas {
        if (numeroPaginas <= 0) {
            throw new ReglaDominioException("El número de páginas debe ser mayor a cero");
        }
    }
}
