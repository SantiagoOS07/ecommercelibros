package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.Nacionalidad;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutorTest {

   

    @Test
    void equals_mismoIdConDatosDistintos_sonIguales() {
        
        Autor autor1 = crearAutor("autor-1");
        Autor autor2 = crearAutor("autor-1");
        autor2.cambiarNombre("Otro Nombre");

       
        assertEquals(autor1, autor2);
    }

    @Test
    void equals_idDiferente_sonDiferentes() {
       
        assertNotEquals(crearAutor("autor-1"), crearAutor("autor-2"));
    }

 

    @Test
    void crear_conDatosValidos_quedaNoEliminadoYConSuNacionalidad() {
       
        Autor autor = crearAutor("autor-1");


        assertFalse(autor.isEliminado());
        assertEquals(List.of(Nacionalidad.COLOMBIANA), autor.getNacionalidades());
    }

    @Test
    void crear_sinId_lanzaReglaDominio() {
       
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear(null, "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA));
    }

    @Test
    void crear_conIdSoloEspacios_lanzaReglaDominio() {
       
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear("   ", "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA));
    }

    @Test
    void crear_sinNombre_lanzaReglaDominio() {
       
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear("autor-1", "", "Biografia", Nacionalidad.COLOMBIANA));
    }

    @Test
    void crear_sinNacionalidad_lanzaReglaDominio() {
       
        assertThrows(ReglaDominioException.class, () ->
                Autor.crear("autor-1", "Gabriel Garcia Marquez", "Biografia", null));
    }

    @Test
    void crear_conBiografiaNula_laPermite() {
       
        Autor autor = Autor.crear("autor-1", "Gabriel Garcia Marquez", null, Nacionalidad.COLOMBIANA);


        assertNull(autor.getBiografia());
    }

    @Test
    void reconstruir_conEliminadoTrue_conservaLaMarca() {
       
        Autor autor = Autor.reconstruir("autor-1", "Nombre", null, List.of(Nacionalidad.CHILENA), true);


        assertTrue(autor.isEliminado());
    }

    // --- Cambios ---

    @Test
    void cambiarNombre_conNombreValido_loActualiza() {
        
        Autor autor = crearAutor("autor-1");

       
        autor.cambiarNombre("Nuevo Nombre");


        assertEquals("Nuevo Nombre", autor.getNombreAutor());
    }

    @Test
    void cambiarNombre_soloConEspacios_lanzaReglaDominioYNoCambia() {
        
        Autor autor = crearAutor("autor-1");

       
        assertThrows(ReglaDominioException.class, () -> autor.cambiarNombre("   "));


        assertEquals("Gabriel Garcia Marquez", autor.getNombreAutor());
    }

    @Test
    void cambiarBiografia_conTexto_laActualiza() {
        
        Autor autor = crearAutor("autor-1");

       
        autor.cambiarBiografia("Nueva biografia");


        assertEquals("Nueva biografia", autor.getBiografia());
    }

    @Test
    void agregarNacionalidad_nueva_laAgrega() {
        
        Autor autor = crearAutor("autor-1");

       
        autor.agregarNacionalidad(Nacionalidad.ESPANOLA);


        assertTrue(autor.getNacionalidades().contains(Nacionalidad.ESPANOLA));
    }

    @Test
    void agregarNacionalidad_repetida_lanzaReglaDominioYNoLaAgrega() {
        
        Autor autor = crearAutor("autor-1");

       
        assertThrows(ReglaDominioException.class, () -> autor.agregarNacionalidad(Nacionalidad.COLOMBIANA));


        assertEquals(1, autor.getNacionalidades().size());
    }

    @Test
    void eliminarNacionalidad_existenteConOtraMas_laQuita() {
        
        Autor autor = crearAutor("autor-1");
        autor.agregarNacionalidad(Nacionalidad.ESPANOLA);

       
        autor.eliminarNacionalidad(Nacionalidad.ESPANOLA);


        assertFalse(autor.getNacionalidades().contains(Nacionalidad.ESPANOLA));
    }

    @Test
    void eliminarNacionalidad_queNoTiene_lanzaReglaDominio() {
        
        Autor autor = crearAutor("autor-1");

       
        assertThrows(ReglaDominioException.class, () -> autor.eliminarNacionalidad(Nacionalidad.JAPONESA));
        assertEquals(1, autor.getNacionalidades().size());
    }

    @Test
    void eliminarNacionalidad_laUnica_lanzaReglaDominio() {
        
        Autor autor = crearAutor("autor-1");

       
        assertThrows(ReglaDominioException.class, () -> autor.eliminarNacionalidad(Nacionalidad.COLOMBIANA));
        assertEquals(List.of(Nacionalidad.COLOMBIANA), autor.getNacionalidades());
    }

    @Test
    void getNacionalidades_intentoDeModificarLaLista_lanzaExcepcion() {
        
        Autor autor = crearAutor("autor-1");

       
        assertThrows(UnsupportedOperationException.class,
                () -> autor.getNacionalidades().add(Nacionalidad.ESPANOLA));
    }

   

    @Test
    void eliminarLogicamente_autorActivo_quedaMarcadoComoEliminado() {
        
        Autor autor = crearAutor("autor-1");

       
        autor.eliminarLogicamente();


        assertTrue(autor.isEliminado());
    }

    @Test
    void eliminarLogicamente_autorYaEliminado_lanzaReglaDominio() {
        
        Autor autor = crearAutor("autor-1");
        autor.eliminarLogicamente();

       
        assertThrows(ReglaDominioException.class, autor::eliminarLogicamente);
        assertTrue(autor.isEliminado());
    }

    @Test
    void cambiarNombre_autorEliminado_lanzaReglaDominioYNoCambia() {
        
        Autor autor = crearAutor("autor-1");
        autor.eliminarLogicamente();

       
        assertThrows(ReglaDominioException.class, () -> autor.cambiarNombre("Otro"));


        assertEquals("Gabriel Garcia Marquez", autor.getNombreAutor());
    }

    @Test
    void cambiarBiografia_autorEliminado_lanzaReglaDominioYNoCambia() {
       
        Autor autor = crearAutor("autor-1");
        autor.eliminarLogicamente();

        
        assertThrows(ReglaDominioException.class, () -> autor.cambiarBiografia("Otra"));

       
        assertEquals("Biografia", autor.getBiografia());
    }

    @Test
    void agregarNacionalidad_autorEliminado_lanzaReglaDominioYNoLaAgrega() {
       
        Autor autor = crearAutor("autor-1");
        autor.eliminarLogicamente();

        
        assertThrows(ReglaDominioException.class, () -> autor.agregarNacionalidad(Nacionalidad.ESPANOLA));

      
        assertFalse(autor.getNacionalidades().contains(Nacionalidad.ESPANOLA));
    }

    private Autor crearAutor(String id) {
        return Autor.crear(id, "Gabriel Garcia Marquez", "Biografia", Nacionalidad.COLOMBIANA);
    }
}