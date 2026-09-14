package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;

import java.util.Objects;

public class Usuario {

    private String idUsuario;
    private String nombre;
    private Correo correo;
    private String contrasenia;
    private Telefono telefono;
    private Direccion direccion;
    private RolUsuario rolUsuario;

    public Usuario(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono, Direccion direccion, RolUsuario rolUsuario) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rolUsuario = rolUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }
}
