package com.uniquindio.ecommercelibros.aplication.dto.request.libro;

import com.uniquindio.ecommercelibros.domain.valueObject.CategoriaLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoLibro;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/** POST /libros */
public record RegistrarLibroRequest(
        @NotBlank(message = "El título del libro es obligatorio")
        String titulo,

        @NotBlank(message = "El id del autor es obligatorio")
        String idAutor,

        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "\\d{13}", message = "El ISBN debe tener 13 dígitos")
        String isbn,

        @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
        String descripcion,

        @NotNull(message = "El número de páginas es obligatorio")
        @Positive(message = "El número de páginas debe ser mayor a cero")
        Integer numeroPaginas,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        Double precio,

        @NotNull(message = "La moneda es obligatoria")
        Moneda moneda,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock,

        @Size(max = 100, message = "La edición no puede superar los 100 caracteres")
        String edicion,

        @NotNull(message = "El id de la editorial es obligatorio")
        UUID idEditorial,

        @NotNull(message = "La fecha de publicación es obligatoria")
        @PastOrPresent(message = "La fecha de publicación no puede ser posterior a la fecha actual")
        LocalDate fechaPublicacion,

        @NotNull(message = "La categoría es obligatoria")
        CategoriaLibro categoria,

        @Size(max = 500, message = "La imagen no puede superar los 500 caracteres")
        String imagen,

        @NotNull(message = "El estado del libro es obligatorio")
        EstadoLibro estado
) {}
