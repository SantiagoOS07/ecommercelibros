
package com.uniquindio.ecommercelibros.aplication.usecase.Carrito;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrarCarrito {

    private final CarritoRepository carritoRepository;
    private final VerificadorDeCarrito verificadorDeCarrito;

    public RegistrarCarrito(
            CarritoRepository carritoRepository,
            VerificadorDeCarrito verificadorDeCarrito) {
        this.carritoRepository = carritoRepository;
        this.verificadorDeCarrito = verificadorDeCarrito;
    }

    @Transactional
    public Carrito ejecutar(String idUsuario) {

        verificadorDeCarrito.verificarQueUsuarioNoTieneCarrito(idUsuario);

        Carrito carrito = Carrito.crear(idUsuario);

        carritoRepository.guardarCarrito(carrito);

        return carrito;
    }
}
