package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ObtenerTitulosCarritos {

    private final LibroRepository libroRepository;

    public ObtenerTitulosCarritos(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, String> ejecutar(Collection<Carrito> carritos) {
        Set<String> isbns = carritos.stream()
                .flatMap(c -> c.getItems().stream())
                .map(item -> item.getIsbnLibro().isbn())
                .collect(Collectors.toSet());

        if (isbns.isEmpty()) {
            return Map.of();
        }
        return libroRepository.buscarTitulosPorIsbn(isbns);
    }
}