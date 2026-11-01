package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DireccionTest {

    @Test
    void crear_conDireccionValida_conservaElValor() {
        
        String valor = "Carrera 14 # 20-35, Apartamento 301";

        
        Direccion direccion = new Direccion(valor);

       
        assertEquals(valor, direccion.direccion());
    }

    @Test
    void crear_conNull_lanzaCasillaVacia() {
        
        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class, () -> new Direccion(null));

        
        assertEquals("La dirección no puede estar vacía", excepcion.getMessage());
    }

    @Test
    void crear_conCadenaVacia_lanzaCasillaVacia() {
       
        assertThrows(CasillaVaciaException.class, () -> new Direccion(""));
    }

    @Test
    void crear_soloConEspacios_lanzaCasillaVacia() {
      
        assertThrows(CasillaVaciaException.class, () -> new Direccion("   "));
    }

    @Test
    void equals_conMismoValor_sonIguales() {
        
        Direccion a = new Direccion("Calle 10 # 15-20");
        Direccion b = new Direccion("Calle 10 # 15-20");

        
        assertEquals(a, b);
    }
}