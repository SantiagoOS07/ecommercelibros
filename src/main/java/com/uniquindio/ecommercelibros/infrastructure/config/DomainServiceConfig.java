
package com.uniquindio.ecommercelibros.infrastructure.config;

import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeLibro;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public VerificadorDeLibro verificadorDeLibro(
            LibroRepository libroRepository) {

        return new VerificadorDeLibro(libroRepository);
    }

    @Bean
    public VerificadorDeCarrito verificadorDeCarrito(
            CarritoRepository carritoRepository) {

        return new VerificadorDeCarrito(carritoRepository);
    }
}
