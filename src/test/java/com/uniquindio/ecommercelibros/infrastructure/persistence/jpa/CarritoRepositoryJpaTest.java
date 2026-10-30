package com.uniquindio.ecommercelibros.infrastructure.persistence.jpa;

import com.uniquindio.ecommercelibros.domain.entity.Carrito;
import com.uniquindio.ecommercelibros.domain.repository.CarritoRepository;
import com.uniquindio.ecommercelibros.domain.valueObject.*;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.entity.CarritoEntity;
import com.uniquindio.ecommercelibros.infrastructure.persistence.jpa.mapper.CarritoPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CarritoRepositoryJpa.class, CarritoPersistenceMapper.class})
class CarritoRepositoryJpaTest {

    private static final ISBN ISBN_A = new ISBN("9783161484100");
    private static final ISBN ISBN_B = new ISBN("9789587580188");

    @Autowired private CarritoRepository repository;
    @Autowired private EntityManager entityManager;

    @Test
    void deberiaGuardarYRecuperarUnCarritoConSusItems() {
        Carrito carrito = carritoConItems(unico("usuario-guardar"));

        guardar(carrito);
        Carrito recuperado = repository.buscarPorIdCarrito(carrito.getIdCarrito()).orElseThrow();

        assertEquals(carrito.getIdUsuario(), recuperado.getIdUsuario());
        assertEquals(EstadoCarrito.CREADO, recuperado.getEstado());
        assertFalse(recuperado.isEliminado());
        assertEquals(2, recuperado.getItems().size());
        assertEquals(Set.of(ISBN_A, ISBN_B),
                recuperado.getItems().stream().map(i -> i.getIsbnLibro()).collect(Collectors.toSet()));
        assertEquals(carrito.getPrecioTotal().monto(), recuperado.getPrecioTotal().monto(), 0.01);
    }

    @Test
    void listarCarritosEnCreado_conDosOMasCarritos_devuelveTodosLosCreadoYNingunProcesando() { // M-16
        Carrito creado1 = carritoConItems(unico("usuario-creado-1"));
        Carrito creado2 = carritoConItems(unico("usuario-creado-2"));
        Carrito procesando = carritoConItems(unico("usuario-procesando"));
        procesando.confirmarCarrito();
        guardar(creado1, creado2, procesando);

        Set<UUID> idsCreado = ids(repository.listarCarritosEnCreado());

        assertTrue(idsCreado.contains(creado1.getIdCarrito()));
        assertTrue(idsCreado.contains(creado2.getIdCarrito()));
        assertFalse(idsCreado.contains(procesando.getIdCarrito()));
    }

    @Test
    void listarCarritosEnProcesando_soloDevuelveLosProcesando() {
        Carrito creado = carritoConItems(unico("usuario-c"));
        Carrito procesando = carritoConItems(unico("usuario-p"));
        procesando.confirmarCarrito();
        guardar(creado, procesando);

        Set<UUID> ids = ids(repository.listarCarritosEnProcesando());

        assertTrue(ids.contains(procesando.getIdCarrito()));
        assertFalse(ids.contains(creado.getIdCarrito()));
    }

    @Test
    void carritoEliminado_noSeBuscaNiSeListaPeroLaFilaSigueExistiendo() {
        Carrito carrito = carritoConItems(unico("usuario-eliminado"));
        guardar(carrito);
        carrito.eliminarLogicamente();
        guardar(carrito);

        assertTrue(repository.buscarPorIdCarrito(carrito.getIdCarrito()).isEmpty());
        assertFalse(ids(repository.listarTodos()).contains(carrito.getIdCarrito()));
        assertFalse(ids(repository.listarCarritosEnCreado()).contains(carrito.getIdCarrito()));
        CarritoEntity fila = entityManager.find(CarritoEntity.class, carrito.getIdCarrito().toString());
        assertNotNull(fila);
        assertTrue(fila.isEliminado());
    }

    @Test
    void buscarPorIdUsuario_ignoraElCarritoEliminadoYDevuelveElNuevo() {
        String usuario = unico("usuario-recrea");
        Carrito viejo = carritoConItems(usuario);
        guardar(viejo);
        viejo.eliminarLogicamente();
        guardar(viejo);
        Carrito nuevo = carritoConItems(usuario);
        guardar(nuevo);

        Carrito encontrado = repository.buscarPorIdUsuario(usuario).orElseThrow();

        assertEquals(nuevo.getIdCarrito(), encontrado.getIdCarrito());
    }

    @Test
    void buscarPorIdUsuario_ignoraLosCarritosVendidos() {
        String usuario = unico("usuario-vendido");
        Carrito vendido = carritoConItems(usuario);
        vendido.confirmarCarrito();
        vendido.completarVentaCarrito();
        guardar(vendido);

        assertTrue(repository.buscarPorIdUsuario(usuario).isEmpty());
    }

    private void guardar(Carrito... carritos) {
        for (Carrito carrito : carritos) {
            repository.guardarCarrito(carrito);
        }
        entityManager.flush();
        entityManager.clear();
    }

    private static Set<UUID> ids(List<Carrito> carritos) {
        return carritos.stream().map(Carrito::getIdCarrito).collect(Collectors.toSet());
    }

    private static String unico(String prefijo) {
        return prefijo + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private static Carrito carritoConItems(String idUsuario) {
        Carrito carrito = Carrito.crear(idUsuario);
        carrito.agregarItem(ISBN_A, 2, new Precio(20000, Moneda.COP), new Stock(10));
        carrito.agregarItem(ISBN_B, 1, new Precio(30000, Moneda.COP), new Stock(10));
        return carrito;
    }
}