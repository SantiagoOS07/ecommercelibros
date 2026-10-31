package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroVentas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarLibrosMasVendidosUseCase {
    private final LibroRepository libroRepository;
    private static final int RANKING_MAXIMO = 10;

    public List<LibroVentas> ejecutar(int limite) {
        if (limite < 1 || limite > RANKING_MAXIMO) {
            throw new ReglaDominioException("El limite debe estar entre 1 " + RANKING_MAXIMO);
        }
        return libroRepository.buscarLibrosMasVendidos(limite);
    }
}
