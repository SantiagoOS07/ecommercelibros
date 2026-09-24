package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ISBNTest {

    @Test
    void debeCrearISBNCon13Caracteres() {
        ISBN isbn =
                new ISBN("9780134685991");

        assertNotNull(isbn);
        assertEquals(
                "9780134685991",
                isbn.isbn()
        );
    }

    @Test
    void debeAceptarISBNDe13CaracteresNumericos() {
        String valor = "1234567890123";

        ISBN isbn = new ISBN(valor);

        assertEquals(
                valor,
                isbn.isbn()
        );
    }

    @Test
    void debeRechazarISBNCon12Caracteres() {
        assertThrows(
                ReglaDominioException.class,
                () -> new ISBN("123456789012")
        );
    }

    @Test
    void debeRechazarISBNCon14Caracteres() {
        assertThrows(
                ReglaDominioException.class,
                () -> new ISBN("12345678901234")
        );
    }

    @Test
    void debeRechazarISBNConMenosDe13Caracteres() {
        assertThrows(
                ReglaDominioException.class,
                () -> new ISBN("123")
        );
    }

    @Test
    void debeRechazarISBNConMasDe13Caracteres() {
        assertThrows(
                ReglaDominioException.class,
                () -> new ISBN("123456789012345")
        );
    }

    @Test
    void debeRechazarISBNFormadoPor13Espacios() {
        assertThrows(
                CasillaVaciaException.class,
                () -> new ISBN("             ")
        );
    }

    @Test
    void debeRechazarISBNFormadoPor13Tabulaciones() {
        assertThrows(
                CasillaVaciaException.class,
                () -> new ISBN("\t\t\t\t\t\t\t\t\t\t\t\t\t")
        );
    }

    @Test
    void debeLanzarNullPointerExceptionCuandoISBNEsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new ISBN(null)
        );
    }

    @Test
    void debeConservarExactamenteElISBNIngresado() {
        String valor = "9780134685991";

        ISBN isbn = new ISBN(valor);

        assertEquals(
                valor,
                isbn.isbn()
        );
    }
}