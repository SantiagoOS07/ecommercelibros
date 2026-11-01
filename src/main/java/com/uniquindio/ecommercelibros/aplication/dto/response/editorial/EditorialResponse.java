package com.uniquindio.ecommercelibros.aplication.dto.response.editorial;

import com.uniquindio.ecommercelibros.domain.valueObject.Pais;

import java.util.UUID;

/** GET /api/editoriales, GET /api/editoriales/{idEditorial} y POST /api/editoriales */
public record EditorialResponse(
        UUID idEditorial,
        String nombreEditorial,
        Pais paisOrigen,
        String descripcionEditorial
) {}