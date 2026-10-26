package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Correo;
import com.uniquindio.ecommercelibros.domain.valueObject.Direccion;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import com.uniquindio.ecommercelibros.domain.valueObject.Telefono;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Usuario {

    private static final String FORMATO_CONTRASENIA = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    private final String idUsuario;
    private String nombre;
    private Correo correo;
    private String contrasenia;
    private Telefono telefono;
    private Direccion direccion;
    private RolUsuario rolUsuario;
    private EstadoUsuario estadoUsuario;

    private Usuario(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono,
                    Direccion direccion, RolUsuario rolUsuario, EstadoUsuario estadoUsuario) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rolUsuario = rolUsuario;
        this.estadoUsuario = estadoUsuario;
    }

 
    public static Usuario crear(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono,
                                Direccion direccion, RolUsuario rolUsuario) {
        validar(idUsuario, nombre, correo, contrasenia, telefono, direccion, rolUsuario);
        return new Usuario(idUsuario, nombre, correo, contrasenia, telefono, direccion, rolUsuario, EstadoUsuario.ACTIVO);
    }

    
    public static Usuario reconstruir(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono,
                                      Direccion direccion, RolUsuario rolUsuario, EstadoUsuario estadoUsuario) {
        return new Usuario(idUsuario, nombre, correo, contrasenia, telefono, direccion, rolUsuario, estadoUsuario);
    }

    private static void validar(String idUsuario, String nombre, Correo correo, String contrasenia, Telefono telefono,
                                Direccion direccion, RolUsuario rolUsuario) {
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
        if (telefono == null) {
            throw new CasillaVaciaException("El telefono del usuario no puede ser nulo.");
        }
        if (direccion == null) {
            throw new CasillaVaciaException("La dirección del usuario no puede ser nula.");
        }
        if (rolUsuario == null) {
            throw new CasillaVaciaException("El rol del usuario no puede ser nulo.");
        }
        validarFormatoContrasenia(contrasenia);
    }

    private static void validarFormatoContrasenia(String contrasenia) {
        if (!contrasenia.matches(FORMATO_CONTRASENIA)) {
            throw new ReglaDominioException(
                    "La contraseña debe tener mínimo 8 caracteres, una mayúscula y un número.");
        }
    }

    public void cambiarNombre(String nuevoNombre) {
        verificarQueEsteActivo();
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new CasillaVaciaException("El nuevo nombre del usuario no puede ser nulo o vacío.");
        }
        this.nombre = nuevoNombre;
    }

    public void cambiarCorreo(Correo nuevoCorreo) {
        verificarQueEsteActivo();
        if (nuevoCorreo == null) {
            throw new CasillaVaciaException("El correo del usuario no puede ser nulo.");
        }
        this.correo = nuevoCorreo;
    }

    public void cambiarContrasenia(String nuevaContrasenia) {
        verificarQueEsteActivo();
        if (nuevaContrasenia == null || nuevaContrasenia.isEmpty()) {
            throw new CasillaVaciaException("La nueva contraseña no puede ser nula o vacía.");
        }
        validarFormatoContrasenia(nuevaContrasenia);
        this.contrasenia = nuevaContrasenia;
    }

    public void cambiarTelefono(Telefono nuevoTelefono) {
        verificarQueEsteActivo();
        if (nuevoTelefono == null) {
            throw new CasillaVaciaException("El telefono del usuario no puede ser nulo.");
        }
        this.telefono = nuevoTelefono;
    }

    public void cambiarDireccion(Direccion nuevaDireccion) {
        verificarQueEsteActivo();
        if (nuevaDireccion == null) {
            throw new CasillaVaciaException("La direccion del usuario no puede ser nula.");
        }
        this.direccion = nuevaDireccion;
    }

    public void cambiarRol(RolUsuario nuevoRol) {
        verificarQueEsteActivo();
        if (nuevoRol == null) {
            throw new CasillaVaciaException("El rol del usuario no puede ser nulo.");
        }
        this.rolUsuario = nuevoRol;
    }

    public boolean coincideContrasenia(String contrasenia) {
        return this.contrasenia.equals(contrasenia);
    }

    
    public void desactivar() {
        verificarTransicion(EstadoUsuario.INACTIVO);
        this.estadoUsuario = EstadoUsuario.INACTIVO;
    }

    public void activar() {
        verificarTransicion(EstadoUsuario.ACTIVO);
        this.estadoUsuario = EstadoUsuario.ACTIVO;
    }

    private void verificarQueEsteActivo() {
        if (this.estadoUsuario.estaInactivo()) {
            throw new ReglaDominioException("Un usuario inactivo no puede modificarse.");
        }
    }

    private void verificarTransicion(EstadoUsuario siguiente) {
        if (!this.estadoUsuario.puedeTransicionarA(siguiente)) {
            throw new ReglaDominioException("El usuario ya se encuentra " + this.estadoUsuario + ".");
        }
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