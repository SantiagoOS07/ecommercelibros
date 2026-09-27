package com.uniquindio.ecommercelibros.aplication.usecase.autor;


import com.uniquindio.ecommercelibros.domain.entity.Autor;
import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;

public class RegistrarAutor {

    private final AutorRepository autorRepository;

    public RegistrarAutor(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void registrar(String idAutor, String nombreAutor, String biografia, Nacionalidad nacionalidad) {
        Autor autor = Autor.crear(idAutor, nombreAutor, biografia, nacionalidad);
        autorRepository.guardarAutor(autor);
    }
}
