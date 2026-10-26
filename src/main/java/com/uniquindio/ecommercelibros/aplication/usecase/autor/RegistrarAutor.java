package com.uniquindio.ecommercelibros.aplication.usecase.autor;


import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeAutor;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrarAutor {

    private final AutorRepository autorRepository;
    private final VerificadorDeAutor verificadorDeAutor;

    public RegistrarAutor(AutorRepository autorRepository, VerificadorDeAutor verificadorDeAutor) {
        this.autorRepository = autorRepository;
        this.verificadorDeAutor = verificadorDeAutor;
    }

    public Autor registrar(String idAutor, String nombreAutor, String biografia, Nacionalidad nacionalidad) {
        verificadorDeAutor.verificarQueAutorNoExiste(idAutor);
        Autor autor = Autor.crear(idAutor, nombreAutor, biografia, nacionalidad);
        autorRepository.guardarAutor(autor);
        return autor;
    }
}