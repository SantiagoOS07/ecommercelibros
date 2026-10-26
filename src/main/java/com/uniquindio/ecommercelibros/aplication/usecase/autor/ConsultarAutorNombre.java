package com.uniquindio.ecommercelibros.aplication.usecase.autor;

import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
public class ConsultarAutorNombre {

    private final AutorRepository autorRepository;

    public ConsultarAutorNombre(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Autor> buscar(String nombreAutor) {
        return autorRepository.buscarPorNombre(nombreAutor);
    }
}
