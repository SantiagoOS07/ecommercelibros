package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeCarrito;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarLibroTest {

    private static final String ISBN_LIBRO = "9783161484100";

    @Mock private LibroRepository libroRepository;
    @Mock private VerificadorDeCarrito verificadorDeCarrito;
    @InjectMocks private EliminarLibro eliminarLibro;

    @Test
    void eliminar_libroExistente_loMarcaGuardaYLoRetiraDeCarritosCreado() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));

        eliminarLibro.eliminar(libro.getIdLibro());

        assertTrue(libro.isEliminado());
        InOrder orden = inOrder(verificadorDeCarrito, libroRepository);
        orden.verify(verificadorDeCarrito).verificarQueLibroNoEstaEnCarritoProcesando(ISBN_LIBRO);
        orden.verify(libroRepository).guardarLibro(libro);
        orden.verify(verificadorDeCarrito).retirarLibroDeCarritosCreado(ISBN_LIBRO);
    }

    @Test
    void eliminar_libroInexistenteOYaEliminado_lanzaNotFoundYNoGuarda() {
        UUID id = UUID.randomUUID();
        when(libroRepository.buscarPorIdLibro(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> eliminarLibro.eliminar(id));
        verify(libroRepository, never()).guardarLibro(any());
        verifyNoInteractions(verificadorDeCarrito);
    }

    @Test
    void eliminar_libroEnCarritoProcesando_lanzaYNoGuarda() {
        Libro libro = libro();
        when(libroRepository.buscarPorIdLibro(libro.getIdLibro())).thenReturn(Optional.of(libro));
        doThrow(new ReglaDominioException("está en un carrito que se está procesando"))
                .when(verificadorDeCarrito).verificarQueLibroNoEstaEnCarritoProcesando(ISBN_LIBRO);

        assertThrows(ReglaDominioException.class, () -> eliminarLibro.eliminar(libro.getIdLibro()));
        assertFalse(libro.isEliminado());
        verify(libroRepository, never()).guardarLibro(any());
        verify(verificadorDeCarrito, never()).retirarLibroDeCarritosCreado(any());
    }

    private static Libro libro() {
        return Libro.crear(new NombreLibro("El Principito"), "autor-123", new ISBN(ISBN_LIBRO),
                "Una historia clásica", new NumeroPaginas(120), new Precio(25000, Moneda.COP),
                new Stock(10), "Primera edición", UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(1943, 4, 6)), CategoriaLibro.NOVELA,
                "imagen.jpg", EstadoLibro.DISPONIBLE);
    }
}