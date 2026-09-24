package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void debeCrearDireccionValida() {
        Direccion direccion =
                new Direccion("Carrera 14 # 20-35");

        assertNotNull(direccion);
        assertEquals(
                "Carrera 14 # 20-35",
                direccion.direccion()
        );
    }

    @Test
    void debeCrearDireccionConCalle() {
        Direccion direccion =
                new Direccion("Calle 10 # 15-20");

        assertEquals(
                "Calle 10 # 15-20",
                direccion.direccion()
        );
    }

    @Test
    void debeCrearDireccionConInformacionAdicional() {
        Direccion direccion =
                new Direccion("Carrera 14 # 20-35, Apartamento 301");

        assertEquals(
                "Carrera 14 # 20-35, Apartamento 301",
                direccion.direccion()
        );
    }

    @Test
    void debePermitirCadenaVaciaSegunLaImplementacionActual() {
        Direccion direccion = new Direccion("");

        assertEquals(
                "",
                direccion.direccion()
        );
    }

    @Test
    void debePermitirEspaciosSegunLaImplementacionActual() {
        Direccion direccion = new Direccion("   ");

        assertEquals(
                "   ",
                direccion.direccion()
        );
    }

    @Test
    void debeLanzarExcepcionCuandoLaDireccionEsNull() {
        CasillaVaciaException excepcion = assertThrows(
                CasillaVaciaException.class,
                () -> new Direccion(null)
        );

        assertEquals(
                "El direccion no puede estar vacia",
                excepcion.getMessage()
        );
    }

    @Test
    void debeConservarExactamenteElValorIngresado() {
        String valor = "Calle 25 # 13-42";

        Direccion direccion = new Direccion(valor);

        assertEquals(valor, direccion.direccion());
    }
}