package com.uniquindio.ecommercelibros.domain.valueObject;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FechaPublicacionTest {

    @Test
    void debeCrearFechaValida() {
        LocalDate fecha =
                LocalDate.of(2025, 5, 10);

        FechaPublicacion publicacion =
                new FechaPublicacion(fecha);

        assertNotNull(publicacion);
        assertEquals(
                fecha,
                publicacion.fecha()
        );
    }

    @Test
    void debePermitirLaFechaActual() {
        LocalDate hoy = LocalDate.now();

        FechaPublicacion publicacion =
                new FechaPublicacion(hoy);

        assertEquals(
                hoy,
                publicacion.fecha()
        );
    }

    @Test
    void debePermitirUnaFechaAnterior() {
        LocalDate fecha =
                LocalDate.now().minusYears(1);

        FechaPublicacion publicacion =
                new FechaPublicacion(fecha);

        assertEquals(
                fecha,
                publicacion.fecha()
        );
    }

    @Test
    void debeRechazarUnaFechaFutura() {
        LocalDate fecha =
                LocalDate.now().plusDays(1);

        assertThrows(
                ReglaDominioException.class,
                () -> new FechaPublicacion(fecha)
        );
    }

    @Test
    void debeRechazarUnaFechaMuyFutura() {
        LocalDate fecha =
                LocalDate.now().plusYears(10);

        assertThrows(
                ReglaDominioException.class,
                () -> new FechaPublicacion(fecha)
        );
    }

    @Test
    void debeRechazarFechaNull() {
        CasillaVaciaException excepcion =
                assertThrows(
                        CasillaVaciaException.class,
                        () -> new FechaPublicacion(null)
                );

        assertEquals(
                "La fecha de publicación no puede ser nula",
                excepcion.getMessage()
        );
    }

    @Test
    void debeObtenerCorrectamenteElDia() {
        FechaPublicacion fecha =
                new FechaPublicacion(
                        LocalDate.of(2025, 9, 23)
                );

        assertEquals(
                23,
                fecha.dia()
        );
    }

    @Test
    void debeObtenerCorrectamenteElMes() {
        FechaPublicacion fecha =
                new FechaPublicacion(
                        LocalDate.of(2025, 9, 23)
                );

        assertEquals(
                9,
                fecha.mes()
        );
    }

    @Test
    void debeObtenerCorrectamenteElAnio() {
        FechaPublicacion fecha =
                new FechaPublicacion(
                        LocalDate.of(2025, 9, 23)
                );

        assertEquals(
                2025,
                fecha.anio()
        );
    }

    @Test
    void debeObtenerCorrectamenteTodosLosComponentesDeLaFecha() {
        FechaPublicacion fecha =
                new FechaPublicacion(
                        LocalDate.of(2020, 12, 31)
                );

        assertEquals(31, fecha.dia());
        assertEquals(12, fecha.mes());
        assertEquals(2020, fecha.anio());
    }
}