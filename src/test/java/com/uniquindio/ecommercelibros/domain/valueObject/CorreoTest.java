package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void debeCrearDireccionCuandoEsValida() {
        Direccion direccion = new Direccion("Carrera 14 # 20-35");

        assertEquals(
                "Carrera 14 # 20-35",
                direccion.direccion()
        );
    }

    @Test
    void debeLanzarExcepcionCuandoLaDireccionEsNula() {
        assertThrows(
                CasillaVaciaException.class,
                () -> new Direccion(null)
        );
    }

    @Test
    void debeConservarElValorDeLaDireccion() {
        String valor = "Calle 10 # 15-20";

        Direccion direccion = new Direccion(valor);

        assertEquals(valor, direccion.direccion());
    }
}