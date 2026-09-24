package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AutorTest {
    @Test
    void dosAutoresConMismoIdDebenSerIgualesAunqueSusDatosCambien() {
        Autor autor1 = crearAutor("autor-1");
        Autor autor2 = crearAutor("autor-1");
        autor2.cambiarNombre("Otro Nombre");

        boolean sonIguales = autor1.equals(autor2);

        assertTrue(sonIguales);
    }

    @Test
    void dosAutoresConIdDiferenteDebenSerDiferentes() {
        Autor autor1 = crearAutor("autor-1");
        Autor autor2 = crearAutor("autor-2");

        boolean sonDiferentes = !autor1.equals(autor2);

        assertTrue(sonDiferentes);
    }

    @Test
    void noDebePermitirCrearAutorSinId() {
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear(null, "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA));
    }

    @Test
    void noDebePermitirCrearAutorSinNombre() {
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear("autor-1", "", "Biografia", Nacionalidad.COLOMBIANA));
    }

    @Test
    void noDebePermitirCrearAutorSinNacionalidad() {
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear("autor-1", "Gabriel Garcia Marquez", "Biografia", null));
    }

    @Test
    void debePermitirCrearAutorConBiografiaNula() {
        Autor autor = Autor.crear("autor-1", "Gabriel Garcia Marquez", null, Nacionalidad.COLOMBIANA);

        assertNull(autor.getBiografia());
    }

    @Test
    void debeCambiarNombreCorrectamente() {
        Autor autor = crearAutor("autor-1");

        autor.cambiarNombre("Nuevo Nombre");

        assertEquals("Nuevo Nombre", autor.getNombreAutor());
    }

    @Test
    void noDebePermitirCambiarNombrePorNuloOVacio() {
        Autor autor = crearAutor("autor-1");
        String nombreOriginal = autor.getNombreAutor();

        assertThrows(ReglaDominioException.class, () -> autor.cambiarNombre(""));
        assertEquals(nombreOriginal, autor.getNombreAutor());
    }

    @Test
    void debeCambiarBiografiaCorrectamente() {
        Autor autor = crearAutor("autor-1");

        autor.cambiarBiografia("Nueva biografia");

        assertEquals("Nueva biografia", autor.getBiografia());
    }

    @Test
    void debeAgregarNacionalidadCorrectamente() {
        Autor autor = crearAutor("autor-1");

        autor.agregarNacionalidad(Nacionalidad.ESPANOLA);

        assertTrue(autor.getNacionalidades().contains(Nacionalidad.ESPANOLA));
    }

    @Test
    void noDebePermitirAgregarNacionalidadRepetida() {
        Autor autor = crearAutor("autor-1");
        int cantidadOriginal = autor.getNacionalidades().size();

        assertThrows(ReglaDominioException.class, () -> autor.agregarNacionalidad(Nacionalidad.COLOMBIANA));
        assertEquals(cantidadOriginal, autor.getNacionalidades().size());
    }

    @Test
    void debeEliminarNacionalidadCorrectamente() {
        Autor autor = crearAutor("autor-1");
        autor.agregarNacionalidad(Nacionalidad.ESPANOLA);

        autor.eliminarNacionalidad(Nacionalidad.ESPANOLA);

        assertFalse(autor.getNacionalidades().contains(Nacionalidad.ESPANOLA));
    }

    @Test
    void noDebePermitirEliminarNacionalidadQueNoExiste() {
        Autor autor = crearAutor("autor-1");
        int cantidadOriginal = autor.getNacionalidades().size();

        assertThrows(ReglaDominioException.class, () -> autor.eliminarNacionalidad(Nacionalidad.JAPONESA));
        assertEquals(cantidadOriginal, autor.getNacionalidades().size());
    }

    private Autor crearAutor(String id) {
        return Autor.crear(id, "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA);
    }
}
