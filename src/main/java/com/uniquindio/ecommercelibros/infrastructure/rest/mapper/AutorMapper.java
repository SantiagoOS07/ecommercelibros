package com.uniquindio.ecommercelibros.infrastructure.rest.mapper;

import com.uniquindio.ecommercelibros.aplication.dto.response.autor.AutorResponse;
import com.uniquindio.ecommercelibros.domain.entity.Autor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutorMapper {

    public AutorResponse toResponse(Autor autor) {
        return new AutorResponse(
                autor.getIdAutor(),
                autor.getNombreAutor(),
                autor.getBiografia(),
                autor.getNacionalidades()
        );
    }

    public List<AutorResponse> toResponseList(List<Autor> autores) {
        return autores.stream()
                .map(this::toResponse)
                .toList();
    }
}