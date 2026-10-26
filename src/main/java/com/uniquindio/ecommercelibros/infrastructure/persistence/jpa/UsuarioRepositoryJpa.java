package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Usuario;
import com.uniquindio.ecommercelibros.domain.repository.UsuarioRepository;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.UsuarioPersistenceMapper;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class UsuarioRepositoryJpa implements UsuarioRepository {

    private final UsuarioJpaDataRepository dataRepository;
    private final UsuarioPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorIdUsuario(String idUsuario) {
        return dataRepository.findById(idUsuario)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return dataRepository.findByCorreoIgnoreCase(correo)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return dataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        dataRepository.save(mapper.toEntity(usuario));
    }
}