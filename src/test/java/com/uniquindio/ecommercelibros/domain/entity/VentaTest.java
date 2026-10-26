package com.uniquindio.ecommercelibros.domain.entity;

import com.uniquindio.ecommercelibros.domain.exception.CasillaVaciaException;
import com.uniquindio.ecommercelibros.domain.exception.ReglaDominioException;
import com.uniquindio.ecommercelibros.domain.valueObject.EstadoPedido;
import com.uniquindio.ecommercelibros.domain.valueObject.ISBN;
import com.uniquindio.ecommercelibros.domain.valueObject.Moneda;
import com.uniquindio.ecommercelibros.domain.valueObject.Precio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    private static final String ID_USUARIO = "user-1";

    @Test
    void crear_conDatosValidos_iniciaEnPreparandoConLaFechaDeHoy() {
        List<ItemVenta> items = List.of(crearItem());
        Precio total = new Precio(50000, Moneda.COP);

        Venta venta = Venta.crear(ID_USUARIO, items, total);

        assertNotNull(venta.getIdVenta());
        assertEquals(ID_USUARIO, venta.getIdUsuario());
        assertEquals(items, venta.getItems());
        assertEquals(total, venta.getTotal());
        assertEquals(LocalDate.now(), venta.getFecha());
        assertEquals(EstadoPedido.PREPARANDO, venta.getEstadoPedido());
    }

    @Test
    void crear_dosVecesConLosMismosDatos_generaEntidadesDiferentes() {
        Venta venta1 = crearVenta();
        Venta venta2 = crearVenta();

        assertNotEquals(venta1, venta2);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void crear_conIdUsuarioNuloOVacio_lanzaCasillaVacia(String idUsuario) {
        List<ItemVenta> items = List.of(crearItem());
        Precio total = new Precio(50000, Moneda.COP);

        assertThrows(CasillaVaciaException.class, () -> Venta.crear(idUsuario, items, total));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void crear_sinItems_lanzaCasillaVacia(List<ItemVenta> items) {
        Precio total = new Precio(50000, Moneda.COP);

        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class,
                () -> Venta.crear(ID_USUARIO, items, total));

        assertEquals("La venta debe tener al menos un item.", excepcion.getMessage());
    }

    @Test
    void crear_conTotalNulo_lanzaCasillaVacia() {
        List<ItemVenta> items = List.of(crearItem());

        CasillaVaciaException excepcion = assertThrows(CasillaVaciaException.class,
                () -> Venta.crear(ID_USUARIO, items, null));

        assertEquals("El total de la venta no puede ser nulo.", excepcion.getMessage());
    }

    @Test
    void crear_copiaLaListaRecibida_porLoQueCambiosPosterioresNoLaAfectan() {
        List<ItemVenta> items = new ArrayList<>(List.of(crearItem()));
        Venta venta = Venta.crear(ID_USUARIO, items, new Precio(50000, Moneda.COP));

        items.add(crearItem());

        assertEquals(1, venta.getItems().size());
    }

    @Test
    void reconstruir_conDatosPersistidos_conservaIdFechaYEstado() {
        UUID id = UUID.randomUUID();
        LocalDate fecha = LocalDate.of(2026, 1, 15);

        Venta venta = Venta.reconstruir(id, ID_USUARIO, List.of(crearItem()),
                new Precio(50000, Moneda.COP), fecha, EstadoPedido.ENVIADO);

        assertEquals(id, venta.getIdVenta());
        assertEquals(fecha, venta.getFecha());
        assertEquals(EstadoPedido.ENVIADO, venta.getEstadoPedido());
    }

    @Test
    void equals_dosVentasConElMismoId_sonIguales() {
        UUID id = UUID.randomUUID();
        Venta venta1 = Venta.reconstruir(id, "user-1", List.of(crearItem()),
                new Precio(50000, Moneda.COP), LocalDate.now(), EstadoPedido.PREPARANDO);
        Venta venta2 = Venta.reconstruir(id, "user-2", List.of(crearItem()),
                new Precio(99999, Moneda.USD), LocalDate.now().minusDays(3), EstadoPedido.ENVIADO);

        assertEquals(venta1, venta2);
        assertEquals(venta1.hashCode(), venta2.hashCode());
    }

    @Test
    void cancelarVenta_enPreparando_pasaACancelado() {
        Venta venta = crearVenta();

        venta.cancelarVenta();

        assertEquals(EstadoPedido.CANCELADO, venta.getEstadoPedido());
    }

    @Test
    void cancelarVenta_enEnviado_lanzaReglaDominioYConservaElEstado() {
        Venta venta = crearVenta();
        venta.actualizarEstadoPedido();

        assertThrows(ReglaDominioException.class, venta::cancelarVenta);

        assertEquals(EstadoPedido.ENVIADO, venta.getEstadoPedido());
    }

    @Test
    void cancelarVenta_yaCancelada_lanzaReglaDominio() {
        Venta venta = crearVenta();
        venta.cancelarVenta();

        assertThrows(ReglaDominioException.class, venta::cancelarVenta);
        assertEquals(EstadoPedido.CANCELADO, venta.getEstadoPedido());
    }

    @Test
    void actualizarEstadoPedido_desdePreparando_pasaAEnviadoSinSaltarseEstados() {
        Venta venta = crearVenta();

        venta.actualizarEstadoPedido();

        assertEquals(EstadoPedido.ENVIADO, venta.getEstadoPedido());
    }

    @Test
    void actualizarEstadoPedido_desdeEnviado_pasaAEntregado() {
        Venta venta = crearVenta();
        venta.actualizarEstadoPedido();

        venta.actualizarEstadoPedido();

        assertEquals(EstadoPedido.ENTREGADO, venta.getEstadoPedido());
    }

    @Test
    void actualizarEstadoPedido_desdeEntregado_lanzaReglaDominio() {
        Venta venta = reconstruirVenta(EstadoPedido.ENTREGADO, LocalDate.now());

        assertThrows(ReglaDominioException.class, venta::actualizarEstadoPedido);
        assertEquals(EstadoPedido.ENTREGADO, venta.getEstadoPedido());
    }

    @Test
    void actualizarEstadoPedido_desdeCancelado_lanzaReglaDominio() {
        Venta venta = crearVenta();
        venta.cancelarVenta();

        assertThrows(ReglaDominioException.class, venta::actualizarEstadoPedido);
        assertEquals(EstadoPedido.CANCELADO, venta.getEstadoPedido());
    }

    // ---------------------------------------- solicitarReembolsoVenta

    @ParameterizedTest
    @NullAndEmptySource
    void solicitarReembolso_conMotivoNuloOVacio_lanzaCasillaVacia(String motivo) {
        Venta venta = crearVenta();
        venta.cancelarVenta();

        assertThrows(CasillaVaciaException.class, () -> venta.solicitarReembolsoVenta(motivo));
        assertEquals(EstadoPedido.CANCELADO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_enPreparando_lanzaReglaDominio() {
        Venta venta = crearVenta();

        assertThrows(ReglaDominioException.class, () -> venta.solicitarReembolsoVenta("Motivo"));
        assertEquals(EstadoPedido.PREPARANDO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_enEnviado_lanzaReglaDominio() {
        Venta venta = crearVenta();
        venta.actualizarEstadoPedido();

        assertThrows(ReglaDominioException.class, () -> venta.solicitarReembolsoVenta("Motivo"));
        assertEquals(EstadoPedido.ENVIADO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_enCancelado_pasaAReembolsado() {
        Venta venta = crearVenta();
        venta.cancelarVenta();

        venta.solicitarReembolsoVenta("Me arrepentí de la compra");

        assertEquals(EstadoPedido.REEMBOLSADO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_entregadoDentroDeLosDiasPermitidos_pasaAReembolsado() {
        Venta venta = reconstruirVenta(EstadoPedido.ENTREGADO, LocalDate.now().minusDays(10));

        venta.solicitarReembolsoVenta("El libro llegó dañado");

        assertEquals(EstadoPedido.REEMBOLSADO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_entregadoEnElUltimoDiaPermitido_pasaAReembolsado() {
        Venta venta = reconstruirVenta(EstadoPedido.ENTREGADO, LocalDate.now().minusDays(30));

        venta.solicitarReembolsoVenta("El libro llegó dañado");

        assertEquals(EstadoPedido.REEMBOLSADO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_entregadoFueraDelPlazo_lanzaReglaDominioYConservaElEstado() {
        Venta venta = reconstruirVenta(EstadoPedido.ENTREGADO, LocalDate.now().minusDays(31));

        ReglaDominioException excepcion = assertThrows(ReglaDominioException.class,
                () -> venta.solicitarReembolsoVenta("El libro llegó dañado"));

        assertTrue(excepcion.getMessage().contains("expirado"));
        assertEquals(EstadoPedido.ENTREGADO, venta.getEstadoPedido());
    }

    @Test
    void solicitarReembolso_yaReembolsada_lanzaReglaDominio() {
        Venta venta = crearVenta();
        venta.cancelarVenta();
        venta.solicitarReembolsoVenta("Primer reembolso");

        assertThrows(ReglaDominioException.class, () -> venta.solicitarReembolsoVenta("Segundo reembolso"));
        assertEquals(EstadoPedido.REEMBOLSADO, venta.getEstadoPedido());
    }

    private static ItemVenta crearItem() {
        return ItemVenta.crear(
                new ISBN("9783161484100"),
                2,
                new Precio(25000, Moneda.COP),
                new Precio(50000, Moneda.COP));
    }

    private static Venta crearVenta() {
        return Venta.crear(ID_USUARIO, List.of(crearItem()), new Precio(50000, Moneda.COP));
    }

    /** Permite fijar fecha y estado (ej. una venta ENTREGADA hace 31 días) sin tocar el reloj. */
    private static Venta reconstruirVenta(EstadoPedido estado, LocalDate fecha) {
        return Venta.reconstruir(UUID.randomUUID(), ID_USUARIO, List.of(crearItem()),
                new Precio(50000, Moneda.COP), fecha, estado);
    }
}