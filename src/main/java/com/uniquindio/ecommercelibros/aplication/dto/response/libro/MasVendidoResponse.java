package com.uniquindio.ecommercelibros.aplication.dto.response.libro;

import com.uniquindio.ecommercelibros.domain.repository.LibroVentas;

public record MasVendidoResponse(String isbnLibro, String tituloLibro, Long cantidadVentas) {
    public static MasVendidoResponse desde(LibroVentas libroVentas) {
        return new MasVendidoResponse(libroVentas.isbnLibro(),
                libroVentas.tituloLibro(),
                libroVentas.cantidadVentas());
    }
}
