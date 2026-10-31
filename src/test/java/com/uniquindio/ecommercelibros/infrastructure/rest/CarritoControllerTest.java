package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.ActualizarCantidadItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.dto.request.carrito.AgregarItemCarritoRequest;
import com.uniquindio.ecommercelibros.aplication.dto.response.carrito.CarritoResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ActualizarCantidadItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.AgregarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.CompletarVentaCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ConsultarCarritoPorID;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.EliminarItemCarrito;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.ObtenerTitulosCarritos;
import com.uniquindio.ecommercelibros.aplication.usecase.Carrito.RegistrarCarrito;
import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.exception.RecursoNoEncontradoException;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.CarritoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoControllerTest {

    @Mock private RegistrarCarrito registrarCarrito;
    @Mock private AgregarItemCarrito agregarItemCarrito;
    @Mock private ActualizarCantidadItemCarrito actualizarCantidadItemCarrito;
    @Mock private EliminarItemCarrito eliminarItemCarrito;
    @Mock private CompletarVentaCarrito completarVentaCarrito;
    @Mock private ConsultarCarritoPorID consultarCarritoPorID;
    @Mock private ObtenerTitulosCarritos obtenerTitulosCarritos;
    @Mock private CarritoMapper mapper;

    @InjectMocks
    private CarritoController controller;

    @BeforeEach
    void configurarSolicitud() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/carritos");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void crearCarrito_deberiaRetornar201() {
        String uid = "usuario123";
        UUID idCarrito = UUID.randomUUID();
        Jwt jwt = Jwt.withTokenValue("token-falso").header("alg", "none").claim("uid", uid).build();
        Carrito carrito = mock(Carrito.class);
        CarritoResponse response = mock(CarritoResponse.class);

        when(registrarCarrito.ejecutar(uid)).thenReturn(carrito);
        when(carrito.getIdCarrito()).thenReturn(idCarrito);
        when(mapper.toResponse(carrito, Map.of())).thenReturn(response);

        ResponseEntity<CarritoResponse> resultado = controller.crear(jwt);

        assertEquals(201, resultado.getStatusCode().value());
        assertNotNull(resultado.getHeaders().getLocation());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void consultarPorId_devuelveElCarritoConSusTitulos() {
        UUID idCarrito = UUID.randomUUID();
        Carrito carrito = mock(Carrito.class);
        CarritoResponse response = mock(CarritoResponse.class);
        Map<String, String> titulos = Map.of("9783161484100", "Libro A");

        when(consultarCarritoPorID.ejecutar(idCarrito)).thenReturn(Optional.of(carrito));
        when(obtenerTitulosCarritos.ejecutar(anyCollection())).thenReturn(titulos);
        when(mapper.toResponse(carrito, titulos)).thenReturn(response);

        ResponseEntity<CarritoResponse> resultado = controller.consultarPorId(idCarrito);

        assertEquals(200, resultado.getStatusCode().value());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void consultarPorId_lanzaNoEncontradoSiNoExiste() {
        UUID idCarrito = UUID.randomUUID();
        when(consultarCarritoPorID.ejecutar(idCarrito)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> controller.consultarPorId(idCarrito));
        verifyNoInteractions(obtenerTitulosCarritos);
    }

    @Test
    void agregarItem_delegaEnElCasoDeUsoYRetorna204() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();

        ResponseEntity<Void> resultado =
                controller.agregarItem(idCarrito, new AgregarItemCarritoRequest(idLibro, 2));

        assertEquals(204, resultado.getStatusCode().value());
        verify(agregarItemCarrito).ejecutar(idCarrito, idLibro, 2);
    }

    @Test
    void actualizarCantidad_tomaLaCantidadDelCuerpo() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();

        ResponseEntity<Void> resultado = controller.actualizarCantidad(
                idCarrito, idLibro, new ActualizarCantidadItemCarritoRequest(3));

        assertEquals(204, resultado.getStatusCode().value());
        verify(actualizarCantidadItemCarrito).ejecutar(idCarrito, idLibro, 3);
    }

    @Test
    void eliminarItem_delegaEnElCasoDeUsoYRetorna204() {
        UUID idCarrito = UUID.randomUUID();
        UUID idLibro = UUID.randomUUID();

        ResponseEntity<Void> resultado = controller.eliminarItem(idCarrito, idLibro);

        assertEquals(204, resultado.getStatusCode().value());
        verify(eliminarItemCarrito).ejecutar(idCarrito, idLibro);
    }

    @Test
    void completarVenta_debeRetornar204() {
        UUID idCarrito = UUID.randomUUID();

        ResponseEntity<Void> resultado = controller.completarVenta(idCarrito);

        assertEquals(204, resultado.getStatusCode().value());
        verify(completarVentaCarrito).ejecutar(idCarrito);
    }
}