package com.uniquindio.ecommercelibros.aplication.usecase.editorial;

import com.uniquindio.ecommercelibros.domain.entity.Editorial;
import com.uniquindio.ecommercelibros.domain.repository.EditorialRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeEditorial;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrarEditorial {

    private final EditorialRepository editorialRepository;
    private final VerificadorDeEditorial verificadorDeEditorial;

    public RegistrarEditorial(EditorialRepository editorialRepository, VerificadorDeEditorial verificadorDeEditorial) {
        this.editorialRepository = editorialRepository;
        this.verificadorDeEditorial = verificadorDeEditorial;
    }

    public Editorial registrar(String nombreEditorial, Pais paisOrigen, String descripcionEditorial) {
        verificadorDeEditorial.verificarQueNombreNoExiste(nombreEditorial);
        Editorial editorial = Editorial.crear(nombreEditorial, paisOrigen, descripcionEditorial);
        editorialRepository.guardarEditorial(editorial);
        return editorial;
    }
}