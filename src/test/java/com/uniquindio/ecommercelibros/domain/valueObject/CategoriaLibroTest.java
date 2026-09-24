package com.uniquindio.ecommercelibros.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaLibroTest {

    @Test
    void debeTenerVeinteCategorias() {
        assertEquals(
                20,
                CategoriaLibro.values().length
        );
    }

    @Test
    void novelaDebeTenerFactorUno() {
        assertEquals(
                1.00,
                CategoriaLibro.NOVELA.factorPrecio()
        );
    }

    @Test
    void programacionDebeTenerFactorUnoPuntoVeinticinco() {
        assertEquals(
                1.25,
                CategoriaLibro.PROGRAMACION.factorPrecio()
        );
    }

    @Test
    void infantilDebeTenerFactorCeroPuntoNoventaYCinco() {
        assertEquals(
                0.95,
                CategoriaLibro.INFANTIL.factorPrecio()
        );
    }

    @Test
    void tecnologiaDebeTenerFactorUnoPuntoVeinte() {
        assertEquals(
                1.20,
                CategoriaLibro.TECNOLOGIA.factorPrecio()
        );
    }

    @Test
    void cienciaDebeTenerFactorUnoPuntoDieciocho() {
        assertEquals(
                1.18,
                CategoriaLibro.CIENCIA.factorPrecio()
        );
    }

    @Test
    void debeCalcularPrecioDeNovela() {
        double precioBase = 100000.0;

        double resultado =
                CategoriaLibro.NOVELA
                        .calcularPrecio(precioBase);

        assertEquals(
                100000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeProgramacion() {
        double precioBase = 100000.0;

        double resultado =
                CategoriaLibro.PROGRAMACION
                        .calcularPrecio(precioBase);

        assertEquals(
                125000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeInfantil() {
        double precioBase = 100000.0;

        double resultado =
                CategoriaLibro.INFANTIL
                        .calcularPrecio(precioBase);

        assertEquals(
                95000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeTecnologia() {
        double precioBase = 100000.0;

        double resultado =
                CategoriaLibro.TECNOLOGIA
                        .calcularPrecio(precioBase);

        assertEquals(
                120000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeHistoria() {
        double precioBase = 50000.0;

        double resultado =
                CategoriaLibro.HISTORIA
                        .calcularPrecio(precioBase);

        assertEquals(
                56000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeFilosofia() {
        double precioBase = 50000.0;

        double resultado =
                CategoriaLibro.FILOSOFIA
                        .calcularPrecio(precioBase);

        assertEquals(
                57500.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeCiencia() {
        double precioBase = 50000.0;

        double resultado =
                CategoriaLibro.CIENCIA
                        .calcularPrecio(precioBase);

        assertEquals(
                59000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeArte() {
        double precioBase = 50000.0;

        double resultado =
                CategoriaLibro.ARTE
                        .calcularPrecio(precioBase);

        assertEquals(
                60000.0,
                resultado
        );
    }

    @Test
    void debeCalcularPrecioDeJuvenil() {
        double precioBase = 50000.0;

        double resultado =
                CategoriaLibro.JUVENIL
                        .calcularPrecio(precioBase);

        assertEquals(
                49000.0,
                resultado
        );
    }

    @Test
    void precioBaseCeroDebeDarCeroParaCualquierCategoria() {
        for (CategoriaLibro categoria :
                CategoriaLibro.values()) {

            assertEquals(
                    0.0,
                    categoria.calcularPrecio(0.0)
            );
        }
    }

    @Test
    void todasLasCategoriasDebenTenerFactorPositivo() {
        for (CategoriaLibro categoria :
                CategoriaLibro.values()) {

            assertTrue(
                    categoria.factorPrecio() > 0
            );
        }
    }

    @Test
    void calcularPrecioDebeCorresponderAlFactorDeCadaCategoria() {
        double precioBase = 100000.0;

        for (CategoriaLibro categoria :
                CategoriaLibro.values()) {

            double esperado =
                    precioBase * categoria.factorPrecio();

            assertEquals(
                    esperado,
                    categoria.calcularPrecio(precioBase)
            );
        }
    }

    @Test
    void todasLasCategoriasDebenTenerFactorDefinido() {
        for (CategoriaLibro categoria :
                CategoriaLibro.values()) {

            assertNotNull(categoria);
            assertTrue(
                    Double.isFinite(categoria.factorPrecio())
            );
        }
    }
}