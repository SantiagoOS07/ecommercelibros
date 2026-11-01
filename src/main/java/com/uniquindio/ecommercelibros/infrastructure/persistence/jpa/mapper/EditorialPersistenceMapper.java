package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.EditorialEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EditorialPersistenceMapper {

    public EditorialEntity toEntity(Editorial editorial) {
        EditorialEntity entity = new EditorialEntity();
        entity.setIdEditorial(editorial.getIdEditorial().toString());
        entity.setNombreEditorial(editorial.getNombreEditorial());
        entity.setPaisOrigen(editorial.getPaisOrigen());
        entity.setDescripcionEditorial(editorial.getDescripcionEditorial());
        return entity;
    }

    public Editorial toDomain(EditorialEntity entity) {
        return Editorial.reconstruir(
                UUID.fromString(entity.getIdEditorial()),
                entity.getNombreEditorial(),
                entity.getPaisOrigen(),
                entity.getDescripcionEditorial()
        );
    }
}