package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.EstadoUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios", indexes = @Index(name = "idx_usuarios_correo", columnList = "correo"))
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @Column(name = "id_usuario", nullable = false, length = 36)
    private String idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "contrasenia_hash", nullable = false, length = 255)
    private String contrasenia;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_usuario", nullable = false, length = 20)
    private RolUsuario rolUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_usuario", nullable = false, length = 20)
    private EstadoUsuario estadoUsuario;
}