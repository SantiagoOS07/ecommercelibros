package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorreoTest {

    @Test
    void debeCrearCorreoValido() {
        Correo correo = new Correo("usuario@gmail.com");

        assertNotNull(correo);
        assertEquals("usuario@gmail.com", correo.correo());
    }

    @Test
    void debeCrearCorreoConNombreYApellido() {
        Correo correo = new Correo("juan.perez@gmail.com");

        assertNotNull(correo);
        assertEquals("juan.perez@gmail.com", correo.correo());
    }

    @Test
    void debeCrearCorreoConMayusculas() {
        Correo correo = new Correo("Usuario@gmail.com");

        assertEquals(
                "Usuario@gmail.com",
                correo.correo()
        );
    }

    @Test
    void debeCrearCorreoConCaracterEspecialPermitido() {
        Correo correo = new Correo("usuario+ventas@gmail.com");

        assertEquals(
                "usuario+ventas@gmail.com",
                correo.correo()
        );
    }

    @Test
    void debeCrearCorreoConGuion() {
        Correo correo = new Correo("usuario-prueba@gmail.com");

        assertEquals(
                "usuario-prueba@gmail.com",
                correo.correo()
        );
    }

    @Test
    void debeCrearCorreoConGuionBajo() {
        Correo correo = new Correo("usuario_prueba@gmail.com");

        assertEquals(
                "usuario_prueba@gmail.com",
                correo.correo()
        );
    }

    @Test
    void debeLanzarExcepcionCuandoElCorreoEsNull() {
        CasillaVaciaException excepcion = assertThrows(
                CasillaVaciaException.class,
                () -> new Correo(null)
        );

        assertEquals(
                "El correo no puede ser vacio",
                excepcion.getMessage()
        );
    }

    @Test
    void debeLanzarExcepcionCuandoNoTieneArroba() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("usuariogmail.com")
        );
    }

    @Test
    void debeLanzarExcepcionCuandoNoTieneDominio() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("usuario@")
        );
    }

    @Test
    void debeLanzarExcepcionCuandoNoTieneExtension() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("usuario@gmail")
        );
    }

    @Test
    void debeLanzarExcepcionCuandoLaExtensionTieneUnaSolaLetra() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("usuario@gmail.c")
        );
    }

    @Test
    void debeLanzarExcepcionCuandoElCorreoEstaVacio() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("")
        );
    }

    @Test
    void debeLanzarExcepcionCuandoElCorreoTieneSoloEspacios() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("   ")
        );
    }

    @Test
    void debeLanzarExcepcionCuandoElCorreoNoTieneFormatoValido() {
        assertThrows(
                ReglaDominioException.class,
                () -> new Correo("correo-invalido")
        );
    }
}