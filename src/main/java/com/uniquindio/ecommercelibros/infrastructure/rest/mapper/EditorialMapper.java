package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.editorial.EditorialResponse;
import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EditorialMapper {

    public EditorialResponse toResponse(Editorial editorial) {
        return new EditorialResponse(
                editorial.getIdEditorial(),
                editorial.getNombreEditorial(),
                editorial.getPaisOrigen(),
                editorial.getDescripcionEditorial()
        );
    }

    public List<EditorialResponse> toResponseList(List<Editorial> editoriales) {
        return editoriales.stream()
                .map(this::toResponse)
                .toList();
    }
}