package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelefonoTest {

    @Test
    void crear_conTelefonoValido_conservaElValor() {
       
        String valor = "3151234567";

     
        Telefono telefono = new Telefono(valor);

      
        assertEquals(valor, telefono.telefono());
    }

    @Test
    void crear_conPrefijoInternacional_conservaElPrefijo() {
     
        Telefono telefono = new Telefono("+57 3151234567");

       
        assertEquals("+57 3151234567", telefono.telefono());
    }

    @Test
    void crear_conNull_lanzaCasillaVacia() {
        
        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class, () -> new Telefono(null));

     
        assertEquals("El teléfono no puede estar vacío", excepcion.getMessage());
    }

    @Test
    void crear_conCadenaVacia_lanzaCasillaVacia() {
       
        assertThrows(CasillaVaciaException.class, () -> new Telefono(""));
    }

    @Test
    void crear_soloConEspacios_lanzaCasillaVacia() {
       
        assertThrows(CasillaVaciaException.class, () -> new Telefono("   "));
    }
}