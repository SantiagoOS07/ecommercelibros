package com.uniquindio.ecommercelibros.infrastructure.security;

import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.UsuarioJpaDataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserConfig {
    
    @Bean
    public UserDetailsService userDetailsService(UsuarioJpaDataRepository usuarioJpaDataRepository) {
        return correo -> usuarioJpaDataRepository.findByCorreoIgnoreCase(correo)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
