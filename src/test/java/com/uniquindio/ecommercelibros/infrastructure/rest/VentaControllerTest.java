package com.uniquindio.ecommercelibros.infrastructure.rest;

import com.uniquindio.ecommercelibros.aplication.dto.response.venta.VentaResponse;
import com.uniquindio.ecommercelibros.aplication.usecase.venta.ActualizarEstadoVentaUseCase;
import com.uniquindio.ecommercelibros.aplication.usecase.venta.CancelarVentaUseCase;
import com.uniquindio.ecommercelibros.aplication.usecase.venta.ConsultarVentaUseCase;
import com.uniquindio.ecommercelibros.aplication.usecase.venta.ConsultarVentasPorUsuarioUseCase;
import com.uniquindio.ecommercelibros.aplication.usecase.venta.SolicitarReembolsoVentaUseCase;
import com.uniquindio.ecommercelibros.domain.entity.Venta;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.infrastructure.rest.VentaController;
import com.uniquindio.ecommercelibros.infrastructure.rest.mapper.VentaMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    private static final String BASE = "/api/ventas";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActualizarEstadoVentaUseCase actualizarEstadoVentaUseCase;

    @MockitoBean
    private CancelarVentaUseCase cancelarVentaUseCase;

    @MockitoBean
    private ConsultarVentasPorUsuarioUseCase consultarVentasPorUsuarioUseCase;

    @MockitoBean
    private ConsultarVentaUseCase consultarVentaUseCase;

    @MockitoBean
    private SolicitarReembolsoVentaUseCase solicitarReembolsoVentaUseCase;

    @MockitoBean
    private VentaMapper mapper;

    @Test
    void deberiaRetornar200ConLaVentaCuandoExiste() throws Exception {
        UUID idVenta = UUID.randomUUID();
        Venta venta = mock(Venta.class);
        when(consultarVentaUseCase.ejecutar(idVenta)).thenReturn(venta);
        when(mapper.toResponseVenta(venta)).thenReturn(crearResponse(idVenta));

        mockMvc.perform(get(BASE + "/{idVenta}", idVenta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenta").value(idVenta.toString()))
                .andExpect(jsonPath("$.estadoPedido").value("PREPARANDO"))
                .andExpect(jsonPath("$.moneda").value("COP"));
    }

    @Test
    void deberiaRetornar404CuandoLaVentaNoExiste() throws Exception {
        UUID idVenta = UUID.randomUUID();
        when(consultarVentaUseCase.ejecutar(idVenta)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get(BASE + "/{idVenta}", idVenta))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deberiaRetornar400CuandoElIdNoEsUnUUID() throws Exception {
        mockMvc.perform(get(BASE + "/{idVenta}", "no-es-un-uuid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(consultarVentaUseCase);
    }

    @Test
    void deberiaRetornar200ConLasVentasDelUsuario() throws Exception {
        Venta venta1 = mock(Venta.class);
        Venta venta2 = mock(Venta.class);
        when(consultarVentasPorUsuarioUseCase.ejecutar("usuario-1"))
                .thenReturn(List.of(venta1, venta2));
        when(mapper.toResponseVentas(anyList()))
                .thenReturn(List.of(crearResponse(UUID.randomUUID()), crearResponse(UUID.randomUUID())));

        mockMvc.perform(get(BASE).param("idUsuario", "usuario-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(consultarVentasPorUsuarioUseCase).ejecutar("usuario-1");
    }

    @Test
    void deberiaRetornarListaVaciaCuandoElUsuarioNoTieneVentas() throws Exception {
        when(consultarVentasPorUsuarioUseCase.ejecutar("usuario-2")).thenReturn(List.of());
        when(mapper.toResponseVentas(anyList())).thenReturn(List.of());

        mockMvc.perform(get(BASE).param("idUsuario", "usuario-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deberiaRetornar400CuandoFaltaElParametroIdUsuario() throws Exception {
        mockMvc.perform(get(BASE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(consultarVentasPorUsuarioUseCase);
    }

    @Test
    void deberiaActualizarElEstadoYRetornar204() throws Exception {
        UUID idVenta = UUID.randomUUID();

        mockMvc.perform(put(BASE + "/{idVenta}/estado", idVenta))
                .andExpect(status().isNoContent());

        verify(actualizarEstadoVentaUseCase).ejecutar(idVenta);
    }

    @Test
    void deberiaRetornar400CuandoElDominioRechazaLaActualizacionDeEstado() throws Exception {
        UUID idVenta = UUID.randomUUID();
        doThrow(new ReglaDominioException("Estado no actualizable"))
                .when(actualizarEstadoVentaUseCase).ejecutar(idVenta);

        mockMvc.perform(put(BASE + "/{idVenta}/estado", idVenta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Estado no actualizable"));
    }

    @Test
    void deberiaRetornar404AlActualizarEstadoDeUnaVentaInexistente() throws Exception {
        UUID idVenta = UUID.randomUUID();
        doThrow(new NoSuchElementException())
                .when(actualizarEstadoVentaUseCase).ejecutar(idVenta);

        mockMvc.perform(put(BASE + "/{idVenta}/estado", idVenta))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaCancelarLaVentaYRetornar204() throws Exception {
        UUID idVenta = UUID.randomUUID();

        mockMvc.perform(put(BASE + "/{idVenta}/cancelar", idVenta))
                .andExpect(status().isNoContent());

        verify(cancelarVentaUseCase).ejecutar(idVenta);
    }

    @Test
    void deberiaRetornar400CuandoElDominioNoPermiteCancelar() throws Exception {
        UUID idVenta = UUID.randomUUID();
        doThrow(new ReglaDominioException("La venta solo puede cancelarse si esta en estado PREPARANDO."))
                .when(cancelarVentaUseCase).ejecutar(idVenta);

        mockMvc.perform(put(BASE + "/{idVenta}/cancelar", idVenta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deberiaRetornar404AlCancelarUnaVentaInexistente() throws Exception {
        UUID idVenta = UUID.randomUUID();
        doThrow(new NoSuchElementException())
                .when(cancelarVentaUseCase).ejecutar(idVenta);

        mockMvc.perform(put(BASE + "/{idVenta}/cancelar", idVenta))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaSolicitarReembolsoYRetornar204() throws Exception {
        UUID idVenta = UUID.randomUUID();
        String requestJson = """
                {
                    "motivo": "El libro llego danado"
                }
                """;

        mockMvc.perform(put(BASE + "/{idVenta}/reembolso", idVenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent());

        verify(solicitarReembolsoVentaUseCase).ejecutar(idVenta, "El libro llego danado");
    }

    @Test
    void deberiaRetornar400CuandoFaltaElMotivo() throws Exception {
        UUID idVenta = UUID.randomUUID();

        mockMvc.perform(put(BASE + "/{idVenta}/reembolso", idVenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(solicitarReembolsoVentaUseCase);
    }

    @Test
    void deberiaRetornar400CuandoElMotivoEstaEnBlanco() throws Exception {
        UUID idVenta = UUID.randomUUID();
        String requestJson = """
                {
                    "motivo": "   "
                }
                """;

        mockMvc.perform(put(BASE + "/{idVenta}/reembolso", idVenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(solicitarReembolsoVentaUseCase);
    }

    @Test
    void deberiaRetornar400CuandoElDominioRechazaElReembolso() throws Exception {
        UUID idVenta = UUID.randomUUID();
        doThrow(new ReglaDominioException("La venta no es reembolsable"))
                .when(solicitarReembolsoVentaUseCase).ejecutar(idVenta, "motivo");

        mockMvc.perform(put(BASE + "/{idVenta}/reembolso", idVenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivo\": \"motivo\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La venta no es reembolsable"));
    }

    private VentaResponse crearResponse(UUID idVenta) {
        return new VentaResponse(
                idVenta.toString(),
                List.of(),
                EstadoPedido.PREPARANDO,
                LocalDate.of(2026, 1, 15),
                50000.0,
                Moneda.COP
        );
    }
}