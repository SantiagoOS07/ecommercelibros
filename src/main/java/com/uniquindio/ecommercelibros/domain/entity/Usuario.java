package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Usuario {

    private final String idUsuario;
    private String nombre;
    private Correo correo;
    private String contrasenia;
    private Telefono telefono;
    private Direccion direccion;
    private RolUsuario rolUsuario;

    private Usuario(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono, Direccion direccion, RolUsuario rolUsuario) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rolUsuario = rolUsuario;
    }

    public static Usuario crear(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono, Direccion direccion, RolUsuario rolUsuario) {
        validar(idUsuario, nombre, correo, contrasenia, rolUsuario);
        return new Usuario(idUsuario, nombre, correo, contrasenia, telefono, direccion, rolUsuario);
    }

    private static void validar(String idUsuario, String nombre, Correo correo, String contrasenia, RolUsuario rolUsuario) {
        if (idUsuario == null || idUsuario.isEmpty()) {
            throw new CasillaVaciaException("El id del usuario no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new CasillaVaciaException("El nombre del usuario no puede ser nulo o vacío.");
        }
        if (correo == null) {
            throw new CasillaVaciaException("El correo del usuario no puede ser nulo.");
        }
        if (contrasenia == null || contrasenia.isEmpty()) {
            throw new CasillaVaciaException("La contraseña del usuario no puede ser nula o vacía.");
        }
        if (rolUsuario == null) {
            throw new CasillaVaciaException("El rol del usuario no puede ser nulo.");
        }
    }

    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new CasillaVaciaException("El nuevo nombre del usuario no puede ser nulo o vacío.");
        }
        this.nombre = nuevoNombre;
    }

    public void cambiarCorreo(Correo nuevoCorreo) {
        if (nuevoCorreo == null) {
            throw new CasillaVaciaException("El correo del usuario no puede ser nulo.");
        }
        this.correo = nuevoCorreo;
    }

    public void cambiarContrasenia(String nuevaContrasenia) {
        if (nuevaContrasenia == null || nuevaContrasenia.isEmpty()) {
            throw new CasillaVaciaException("La nueva contraseña no puede ser nula o vacía.");
        }
        this.contrasenia = nuevaContrasenia;
    }

    public void cambiarTelefono(Telefono nuevoTelefono) {
        if (nuevoTelefono == null) {
            throw new CasillaVaciaException("El telefono del usuario no puede ser nulo.");
        }
        this.telefono = nuevoTelefono;
    }

    public void cambiarDireccion(Direccion nuevaDireccion) {
        if(nuevaDireccion == null) {
            throw new CasillaVaciaException("La direccion del usuario no puede ser nula.");
        }
        this.direccion = nuevaDireccion;
    }

    public void cambiarRol(RolUsuario nuevoRol) {
        if (nuevoRol == null) {
            throw new CasillaVaciaException("El rol del usuario no puede ser nulo.");
        }
        this.rolUsuario = nuevoRol;
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