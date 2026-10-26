package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.AgregarItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.CarritoResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.AgregarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ActualizarCantidadItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.CompletarVentaCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.libro.ConsultarLibroID;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.entity.Libro;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarritoControllerTest {

    @Mock
    private RegistrarCarrito registrarCarrito;

    @Mock
    private AgregarItemCarrito agregarItemCarrito;

    @Mock
    private ActualizarCantidadItemCarrito actualizarCantidadItemCarrito;

    @Mock
    private EliminarItemCarrito eliminarItemCarrito;

    @Mock
    private CompletarVentaCarrito completarVentaCarrito;

    @Mock
    private ConsultarLibroID consultarLibroID;

    @Mock
    private CarritoMapper mapper;

    @InjectMocks
    private CarritoController controller;

    @BeforeEach
    void configurarSolicitud() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/carritos");
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(request)
        );
    }

    @Test
    void crearCarrito_debeRetornar201() {
        String idUsuario = "usuario-123";
        UUID idCarrito = UUID.randomUUID();

        Carrito carrito = mock(Carrito.class);
        CarritoResponse carritoResponse = mock(CarritoResponse.class);

        when(registrarCarrito.ejecutar(idUsuario)).thenReturn(carrito);
        when(carrito.getIdCarrito()).thenReturn(idCarrito);

        when(mapper.toResponse(carrito, Map.of()))
                .thenReturn(carritoResponse);

        ResponseEntity<?> resultado = controller.crear(idUsuario);

        assertEquals(201, resultado.getStatusCode().value());
        assertSame(carritoResponse, resultado.getBody());

        assertEquals(
                "/api/carritos/" + idCarrito,
                resultado.getHeaders().getLocation().getPath()
        );

        verify(registrarCarrito).ejecutar(idUsuario);
        verify(mapper).toResponse(carrito, Map.of());
    }

    @Test
    void agregarItem_debeRetornar204() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        int cantidad = 2;

        AgregarItemCarritoRequest request =
                mock(AgregarItemCarritoRequest.class);
        Libro libro = mock(Libro.class);

        when(request.idLibro()).thenReturn(idLibro);
        when(request.cantidad()).thenReturn(cantidad);
        when(consultarLibroID.ConsultarLibroPorID(idLibro))
                .thenReturn(Optional.of(libro));

        ResponseEntity<Void> resultado =
                controller.agregarItem(idCarrito, request);

        assertEquals(204, resultado.getStatusCode().value());

        verify(consultarLibroID).ConsultarLibroPorID(idLibro);
        verify(agregarItemCarrito).ejecutar(
                eq(idCarrito),
                isNull(),
                eq(cantidad),
                isNull(),
                isNull()
        );
    }

    @Test
    void actualizarCantidad_debeRetornar204() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();
        int cantidad = 3;

        Libro libro = mock(Libro.class);

        when(consultarLibroID.ConsultarLibroPorID(idLibro))
                .thenReturn(Optional.of(libro));

        ResponseEntity<Void> resultado =
                controller.actualizarCantidad(
                        idCarrito, idLibro, cantidad
                );

        assertEquals(204, resultado.getStatusCode().value());

        verify(actualizarCantidadItemCarrito).ejecutar(
                eq(idCarrito),
                isNull(),
                eq(cantidad),
                isNull()
        );
    }

    @Test
    void eliminarItem_debeRetornar204() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();

        Libro libro = mock(Libro.class);

        when(consultarLibroID.ConsultarLibroPorID(idLibro))
                .thenReturn(Optional.of(libro));

        ResponseEntity<Void> resultado =
                controller.eliminarItem(idCarrito, idLibro);

        assertEquals(204, resultado.getStatusCode().value());

        verify(eliminarItemCarrito).ejecutar(
                eq(idCarrito),
                isNull()
        );
    }

    @Test
    void completarVenta_debeRetornar204() {
        UUID idCarrito = UUID.randomUUID();

        ResponseEntity<Void> resultado =
                controller.completarVenta(idCarrito);

        assertEquals(204, resultado.getStatusCode().value());

        verify(completarVentaCarrito).ejecutar(idCarrito);
    }
}
