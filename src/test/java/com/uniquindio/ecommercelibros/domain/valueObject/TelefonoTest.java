package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelefonoTest {

    @Test
    void debeCrearTelefonoValido() {
        Telefono telefono =
                new Telefono("3151234567");

        assertNotNull(telefono);
        assertEquals(
                "3151234567",
                telefono.telefono()
        );
    }

    @Test
    void debeCrearTelefonoMovilColombiano() {
        Telefono telefono =
                new Telefono("3001234567");

        assertEquals(
                "3001234567",
                telefono.telefono()
        );
    }

    @Test
    void debeConservarElPrefijoSiEsIngresado() {
        Telefono telefono =
                new Telefono("+57 3151234567");

        assertEquals(
                "+57 3151234567",
                telefono.telefono()
        );
    }

    @Test
    void debePermitirCadenaVaciaSegunLaImplementacionActual() {
        Telefono telefono = new Telefono("");

        assertEquals(
                "",
                telefono.telefono()
        );
    }

    @Test
    void debePermitirEspaciosSegunLaImplementacionActual() {
        Telefono telefono = new Telefono("   ");

        assertEquals(
                "   ",
                telefono.telefono()
        );
    }

    @Test
    void debeLanzarExcepcionCuandoElTelefonoEsNull() {
        CasillaVaciaException excepcion = assertThrows(
                CasillaVaciaException.class,
                () -> new Telefono(null)
        );

        assertEquals(
                "El telefono no puede estar vacio",
                excepcion.getMessage()
        );
    }

    @Test
    void debeConservarExactamenteElValorIngresado() {
        String valor = "3124567890";

        Telefono telefono = new Telefono(valor);

        assertEquals(
                valor,
                telefono.telefono()
        );
    }
}