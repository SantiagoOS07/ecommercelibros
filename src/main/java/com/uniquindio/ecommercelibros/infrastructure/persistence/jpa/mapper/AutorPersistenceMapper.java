package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.AutorEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AutorPersistenceMapper {

    public AutorEntity toEntity(Autor autor) {
        AutorEntity entity = new AutorEntity();
        entity.setIdAutor(autor.getIdAutor());
        entity.setNombreAutor(autor.getNombreAutor());
        entity.setBiografia(autor.getBiografia());
        entity.setNacionalidades(new ArrayList<>(autor.getNacionalidades()));
        return entity;
    }

    public Autor toDomain(AutorEntity entity) {
        return Autor.reconstruir(
                entity.getIdAutor(),
                entity.getNombreAutor(),
                entity.getBiografia(),
                entity.getNacionalidades()
        );
    }
}