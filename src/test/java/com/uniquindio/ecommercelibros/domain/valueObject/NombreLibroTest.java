package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NombreLibroTest {

    @Test
    void debeCrearNombreDeLibroValido() {
        NombreLibro nombre =
                new NombreLibro("Cien años de soledad");

        assertNotNull(nombre);
        assertEquals(
                "Cien años de soledad",
                nombre.nombreLibro()
        );
    }

    @Test
    void debeCrearNombreDeLibroCorto() {
        NombreLibro nombre =
                new NombreLibro("1984");

        assertEquals(
                "1984",
                nombre.nombreLibro()
        );
    }

    @Test
    void debeCrearNombreConCaracteresEspeciales() {
        NombreLibro nombre =
                new NombreLibro("¿Quién se ha llevado mi queso?");

        assertEquals(
                "¿Quién se ha llevado mi queso?",
                nombre.nombreLibro()
        );
    }

    @Test
    void debePermitirNombreVacioSegunLaImplementacionActual() {
        NombreLibro nombre =
                new NombreLibro("");

        assertEquals(
                "",
                nombre.nombreLibro()
        );
    }

    @Test
    void debePermitirEspaciosSegunLaImplementacionActual() {
        NombreLibro nombre =
                new NombreLibro("   ");

        assertEquals(
                "   ",
                nombre.nombreLibro()
        );
    }

    @Test
    void debeLanzarExcepcionCuandoElNombreEsNull() {
        CasillaVaciaException excepcion = assertThrows(
                CasillaVaciaException.class,
                () -> new NombreLibro(null)
        );

        assertEquals(
                "El nombre del libro no puede ser vacio",
                excepcion.getMessage()
        );
    }

    @Test
    void debeConservarExactamenteElNombreIngresado() {
        String nombreOriginal =
                "El amor en los tiempos del cólera";

        NombreLibro nombre =
                new NombreLibro(nombreOriginal);

        assertEquals(
                nombreOriginal,
                nombre.nombreLibro()
        );
    }
}