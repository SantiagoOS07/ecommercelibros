package com.uniquindio.ecommercelibros.aplication.dto.response;

import com.uniquindio.ecommercelibros.domain.repository.Pagina;

import java.util.List;
import java.util.function.Function;

public record PaginaResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int size,
        int number
) {
    public static <D, T> PaginaResponse<T> desde(Pagina<D> pagina, Function<D, T> convertidor) {
        return new PaginaResponse<>(
                pagina.contenido().stream().map(convertidor).toList(),
                pagina.totalElementos(),
                pagina.totalPaginas(),
                pagina.tamano(),
                pagina.numero());
    }
}
