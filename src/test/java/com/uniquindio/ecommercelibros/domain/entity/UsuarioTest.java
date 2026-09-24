package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void dosUsuariosConMismoIdDebenSerIgualesAunqueSusDatosCambien() {
        Usuario usuario1 = crearUsuario("user-1");
        Usuario usuario2 = crearUsuario("user-1");
        usuario2.cambiarNombre("Otro Nombre");

        boolean sonIguales = usuario1.equals(usuario2);

        assertTrue(sonIguales);
    }

    @Test
    void dosUsuariosConIdDiferenteDebenSerDiferentes() {
        Usuario usuario1 = crearUsuario("user-1");
        Usuario usuario2 = crearUsuario("user-2");

        boolean sonDiferentes = !usuario1.equals(usuario2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearUsuarioSinId() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear(null, "Juan Perez", new Correo("juan@mail.com"), "clave123",
                        new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE));
    }

    @Test
    void noDebePermitirCrearUsuarioSinNombre() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear("user-1", "", new Correo("juan@mail.com"), "clave123",
                        new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE));
    }

    @Test
    void noDebePermitirCrearUsuarioSinCorreo() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear("user-1", "Juan Perez", null, "clave123",
                        new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE));
    }

    @Test
    void noDebePermitirCrearUsuarioSinContrasenia() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "",
                        new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE));
    }

    @Test
    void noDebePermitirCrearUsuarioSinRol() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "clave123",
                        new Telefono("3001234567"), new Direccion("Calle 1"), null));
    }

    @Test
    void noDebePermitirCrearUsuarioSinDireccion() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "clave123",
                        new Telefono("3001234567"), null, RolUsuario.CLIENTE));
    }

    @Test
    void noDebePermitirCrearUsuarioSinTelefono() {
        assertThrows(CasillaVaciaException.class, () ->
                Usuario.crear("user-1", "Juan Perez", new Correo("juan@mail.com"), "clave123",
                        null, new Direccion("Calle 1"), RolUsuario.CLIENTE));
    }

    @Test
    void debeCambiarNombreCorrectamente() {
        Usuario usuario = crearUsuario("user-1");

        usuario.cambiarNombre("Nuevo Nombre");

        assertEquals("Nuevo Nombre", usuario.getNombre());
    }

    @Test
    void noDebePermitirCambiarNombrePorNuloOVacio() {
        Usuario usuario = crearUsuario("user-1");
        String nombreOriginal = usuario.getNombre();

        assertThrows(CasillaVaciaException.class, () -> usuario.cambiarNombre(""));
        assertEquals(nombreOriginal, usuario.getNombre());
    }

    @Test
    void debeCambiarCorreoCorrectamente() {
        Usuario usuario = crearUsuario("user-1");
        Correo nuevoCorreo = new Correo("nuevo@mail.com");

        usuario.cambiarCorreo(nuevoCorreo);

        assertEquals(nuevoCorreo, usuario.getCorreo());
    }

    @Test
    void noDebePermitirCambiarCorreoPorUnoNulo() {
        Usuario usuario = crearUsuario("user-1");
        Correo correoOriginal = usuario.getCorreo();

        assertThrows(CasillaVaciaException.class, () -> usuario.cambiarCorreo(null));
        assertEquals(correoOriginal, usuario.getCorreo());
    }

    @Test
    void debeCambiarContraseniaCorrectamente() {
        Usuario usuario = crearUsuario("user-1");

        usuario.cambiarContrasenia("nuevaClave456");

        assertEquals("nuevaClave456", usuario.getContrasenia());
    }

    @Test
    void noDebePermitirCambiarContraseniaPorNulaOVacia() {
        Usuario usuario = crearUsuario("user-1");
        String contraseniaOriginal = usuario.getContrasenia();

        assertThrows(CasillaVaciaException.class, () -> usuario.cambiarContrasenia(""));
        assertEquals(contraseniaOriginal, usuario.getContrasenia());
    }

    @Test
    void debeCambiarTelefonoCorrectamente() {
        Usuario usuario = crearUsuario("user-1");
        Telefono nuevoTelefono = new Telefono("3109876543");

        usuario.cambiarTelefono(nuevoTelefono);

        assertEquals(nuevoTelefono, usuario.getTelefono());
    }

    @Test
    void noDebePermitirCambiarTelefonoPorUnoNulo() {
        Usuario usuario = crearUsuario("user-1");
        Telefono telefonoOriginal = usuario.getTelefono();

        assertThrows(CasillaVaciaException.class, () -> usuario.cambiarTelefono(null));
        assertEquals(telefonoOriginal, usuario.getTelefono());
    }

    @Test
    void debeCambiarDireccionCorrectamente() {
        Usuario usuario = crearUsuario("user-1");
        Direccion nuevaDireccion = new Direccion("Carrera 20");

        usuario.cambiarDireccion(nuevaDireccion);

        assertEquals(nuevaDireccion, usuario.getDireccion());
    }

    @Test
    void noDebePermitirCambiarDireccionPorUnaNula() {
        Usuario usuario = crearUsuario("user-1");
        Direccion direccionOriginal = usuario.getDireccion();

        assertThrows(CasillaVaciaException.class, () -> usuario.cambiarDireccion(null));
        assertEquals(direccionOriginal, usuario.getDireccion());
    }

    @Test
    void debeCambiarRolCorrectamente() {
        Usuario usuario = crearUsuario("user-1");

        usuario.cambiarRol(RolUsuario.ADMINISTRADOR);

        assertEquals(RolUsuario.ADMINISTRADOR, usuario.getRolUsuario());
    }

    @Test
    void noDebePermitirCambiarRolPorUnoNulo() {
        Usuario usuario = crearUsuario("user-1");
        RolUsuario rolOriginal = usuario.getRolUsuario();

        assertThrows(CasillaVaciaException.class, () -> usuario.cambiarRol(null));
        assertEquals(rolOriginal, usuario.getRolUsuario());
    }

    private Usuario crearUsuario(String id) {
        return Usuario.crear(id, "Juan Perez", new Correo("juan@mail.com"), "clave123",
                new Telefono("3001234567"), new Direccion("Calle 1"), RolUsuario.CLIENTE);
    }
}