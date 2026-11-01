package com.uniquindio.ecommercelibros.domain.repository;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EditorialRepository {

    Optional<Editorial> buscarPorId(UUID idEditorial);

    List<Editorial> listarTodas();

    void guardarEditorial(Editorial editorial);

    boolean existePorNombre(String nombreEditorial);
}