package com.uniquindio.ecommercelibros.aplication.dto.response.autor;

import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;

import java.util.List;

/** GET /autores, GET /autores/{idAutor} y GET /autores/nombre/{nombreAutor} */
public record AutorResponse(
        String idAutor,
        String nombreAutor,
        String biografia,
        List<Nacionalidad> nacionalidades
) {}
