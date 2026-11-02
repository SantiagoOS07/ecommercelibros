package com.uniquindio.ecommercelibros.aplication.usecase.carrito;

import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ObtenerTitulosCarritos;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import com.uniquindio.ecommercelibros.domain.valueObject.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ObtenerTitulosCarritosTest {

    private LibroRepository libroRepository;
    private ObtenerTitulosCarritos obtenerTitulos;

    @BeforeEach
    void setUp() {
        libroRepository = mock(LibroRepository.class);
        obtenerTitulos = new ObtenerTitulosCarritos(libroRepository);
    }

    private Carrito carrito(String usuario, String... isbns) {
        Carrito c = Carrito.crear(usuario);
        for (String isbn : isbns) {
            c.agregarItem(new ISBN(isbn), 1, new Precio(1000, Moneda.COP), new Stock(5));
        }
        return c;
    }

    @Test
    void hace_una_sola_consulta_para_todos_los_carritos() {
        Carrito a = carrito("u-1", "9783161484100", "9789580010011");
        Carrito b = carrito("u-2", "9783161484100");
        Map<String, String> esperado = Map.of(
                "9783161484100", "Libro A", "9789580010011", "Libro B");
        when(libroRepository.buscarTitulosPorIsbn(Set.of("9783161484100", "9789580010011")))
                .thenReturn(esperado);

        Map<String, String> resultado = obtenerTitulos.ejecutar(List.of(a, b));

        assertEquals(esperado, resultado);
        verify(libroRepository, times(1)).buscarTitulosPorIsbn(anyCollection());
    }

    @Test
    void sin_items_no_consulta_el_repositorio() {
        Map<String, String> resultado = obtenerTitulos.ejecutar(List.of(Carrito.crear("u-1")));

        assertTrue(resultado.isEmpty());
        verifyNoInteractions(libroRepository);
    }
}