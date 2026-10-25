package com.uniquindio.ecommercelibros.aplication.usecase.libro;

import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.repository.LibroRepository;
import com.uniquindio.ecommercelibros.domain.service.VerificadorDeLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrarLibroUseCaseTest {

    private static final String ISBN_PRUEBA = "9780306406157";

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private VerificadorDeLibro verificadorDeLibro;

    @InjectMocks
    private RegistrarLibroUseCase useCase;

    @Test
    @DisplayName("Registrar un libro con ISBN libre lo guarda una sola vez")
    void registrarConIsbnLibre_guardaUnaVez() {
        // Arrange
        Libro libro = libro();

        // Act
        Libro resultado = useCase.registrarLibro(libro);

        // Assert
        assertThat(resultado).isSameAs(libro);
        verify(libroRepository).guardarLibro(libro);
    }

    @Test
    @DisplayName("Registrar un libro con ISBN repetido lanza excepción y nunca guarda")
    void registrarConIsbnRepetido_lanzaExcepcion_yNoGuarda() {
        // Arrange
        Libro libro = libro();
        doThrow(new ReglaDominioException("Ya existe un libro con ese ISBN"))
                .when(verificadorDeLibro).verificarQueISBNNoExiste(ISBN_PRUEBA);

        // Act + Assert
        assertThatThrownBy(() -> useCase.registrarLibro(libro))
                .isInstanceOf(ReglaDominioException.class)
                .hasMessage("Ya existe un libro con ese ISBN");

        verify(libroRepository, never()).guardarLibro(any());
    }

    private static Libro libro() {
        return Libro.crear(
                new NombreLibro("Fundamentos de programación"),
                UUID.randomUUID().toString(),
                new ISBN(ISBN_PRUEBA),
                "Descripción de prueba",
                new NumeroPaginas(320),
                new Precio(45000.50, Moneda.COP),
                new Stock(12),
                "Primera edición",
                UUID.randomUUID(),
                new FechaPublicacion(LocalDate.of(2020, 5, 10)),
                CategoriaLibro.values()[0],
                "https://imagenes.test/libro.png",
                EstadoLibro.values()[0]);
    }
}