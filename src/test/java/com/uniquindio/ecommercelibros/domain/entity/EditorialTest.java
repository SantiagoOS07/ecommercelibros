package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Pais;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditorialTest {

    @Test
    void dosEditorialesConDatosIgualesDebenSerEntidadesDiferentes() {
        Editorial editorial1 = crearEditorial();
        Editorial editorial2 = crearEditorial();

        boolean sonDiferentes = !editorial1.equals(editorial2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearEditorialSinNombre() {
        assertThrows(ReglaDominioException.class, () ->
                Editorial.crear("", Pais.COLOMBIA, "Editorial reconocida"));
    }

    @Test
    void noDebePermitirCrearEditorialSinPaisOrigen() {
        assertThrows(ReglaDominioException.class, () ->
                Editorial.crear("Planeta", null, "Editorial reconocida"));
    }

    @Test
    void debeCambiarNombreCorrectamente() {
        Editorial editorial = crearEditorial();

        editorial.cambiarNombre("Nueva Editorial");

        assertEquals("Nueva Editorial", editorial.getNombreEditorial());
    }

    @Test
    void noDebePermitirCambiarNombrePorNuloOVacio() {
        Editorial editorial = crearEditorial();
        String nombreOriginal = editorial.getNombreEditorial();

        assertThrows(ReglaDominioException.class, () -> editorial.cambiarNombre(""));
        assertEquals(nombreOriginal, editorial.getNombreEditorial());
    }

    @Test
    void debeCambiarDescripcionCorrectamente() {
        Editorial editorial = crearEditorial();

        editorial.cambiarDescripcion("Nueva descripcion");

        assertEquals("Nueva descripcion", editorial.getDescripcionEditorial());
    }

    private Editorial crearEditorial() {
        return Editorial.crear("Planeta", Pais.COLOMBIA, "Editorial reconocida");
    }
}