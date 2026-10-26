
package com.uniquindio.ecommercelibros.infrastructure.config;

import com.uniquindio.ecommercelibros.domain.repository.AutorRepository;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeAutor;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeLibro;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeUsuario;

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

    @Bean
    public VerificadorDeUsuario verificadorDeUsuario(
            UsuarioRepository usuarioRepository) {

        return new VerificadorDeUsuario(usuarioRepository);
    }

    @Bean
    public VerificadorDeAutor verificadorDeAutor(
            AutorRepository autorRepository,
            LibroRepository libroRepository) {

        return new VerificadorDeAutor(autorRepository, libroRepository);
    }
}