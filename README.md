# ECOMMERCE LIBROS

**Nicho:** ecommerce de libros. Los proveedores publican libros en el catálogo y los clientes los compran mediante un carrito, con ventas, entregas y reembolsos.

**Integrantes:** Santiago Ospina Sanchez, Sharif Giraldo Obando, Juan Sebastian Hernandez

**Materia:** Programación Avanzada · Universidad del Quindío

## Tecnologías

Java 21, Spring Boot 4, Spring Security con JWT, Spring Data JPA, H2 en memoria, Swagger (springdoc), JUnit 5 y Mockito. Arquitectura hexagonal: `domain`, `aplication`, `infrastructure`.

## Cómo ejecutar

1. Clonar el repositorio y entrar a la carpeta del proyecto.
2. Ejecutar el archivo para iniciar la aplicación: `EcommercelibrosApplication.java`
3. Abrir Swagger: `http://localhost:8080/swagger-ui.html`
4. Consola de H2: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:ecommercedb`, usuario `sa`, contraseña vacía).
5. Ejecutar las pruebas:

```
./gradlew test
```

Al arrancar se cargan datos de prueba desde `src/main/resources/data/` (`usuarios-autores.sql`, `libros-carritos.sql`, `ventas.sql`). La clave del JWT se toma de la variable `JWT_SECRET` (hay un valor por defecto en `application.properties`) y la duración del token está en `jwt.expiry` (60 minutos).

Para usar el token en Swagger: hacer `POST /api/auth/login`, copiar el valor de `token`, pulsar **Authorize** y pegarlo.

## Usuarios de prueba

| Rol | Correo | Contraseña |
| --- | --- | --- |
| ADMINISTRADOR | admin@libros.com | Admin12345 |
| PROVEEDOR (vendedor) | proveedor@libros.com | Proveedor123 |
| CLIENTE (comprador) | cliente1@libros.com | Cliente123 |
| CLIENTE (comprador) | cliente2@libros.com | Cliente123 |
| CLIENTE inactivo (no puede iniciar sesión) | inactivo@libros.com | Inactivo123 |

`cliente1@libros.com` es dueño de un carrito de prueba en estado CREADO.

## Endpoints principales

Todos los endpoints cuelgan de `/api`. Sin token responde 401 y con un rol sin permiso responde 403.

| Recurso | Endpoint | Quién puede |
| --- | --- | --- |
| Auth | `POST /auth/login` | Público |
| Usuarios | `POST /usuarios` (registro como CLIENTE) | Público |
| | `GET /usuarios/{id}`, `PUT /usuarios/{id}/nombre`, `/correo`, `/telefono`, `/direccion`, `/contrasenia` | El propio usuario o ADMINISTRADOR |
| | `GET /usuarios`, `GET /usuarios/correo/{correo}`, `PUT /usuarios/{id}/rol`, `PUT /usuarios/{id}/reactivar`, `DELETE /usuarios/{id}` | ADMINISTRADOR |
| Libros | `GET /libros` (paginado con filtros), `GET /libros/{id}`, `/isbn/{isbn}`, `/nombre/{nombre}`, `/autor/{nombreAutor}`, `/mas-vendidos` | Público |
| | `POST /libros`, `PUT /libros/{id}/titulo`, `/descripcion`, `/numero-paginas`, `/precio`, `/stock`, `/edicion`, `/editorial`, `/fecha-publicacion`, `/categoria`, `/imagen`, `/agotar`, `/reactivar`, `/descontinuar`, `DELETE /libros/{id}` | PROVEEDOR o ADMINISTRADOR |
| Autores | `GET /autores`, `/autores/{id}`, `/autores/nombre/{nombre}`, `/autores/{id}/libros` | Público |
| | `POST /autores`, `PUT /autores/{id}/nombre`, `/biografia`, `POST /autores/{id}/nacionalidades`, `DELETE /autores/{id}/nacionalidades/{nacionalidad}` | PROVEEDOR o ADMINISTRADOR |
| | `DELETE /autores/{id}` | ADMINISTRADOR |
| Editoriales | `GET /editoriales`, `GET /editoriales/{id}` | Autenticado |
| | `POST /editoriales` | PROVEEDOR o ADMINISTRADOR |
| Carritos | `POST /carritos`, `GET /carritos/{id}`, `GET /carritos?idUsuario=`, `POST /carritos/{id}/items`, `PUT /carritos/{id}/items/{idLibro}`, `DELETE /carritos/{id}/items/{idLibro}`, `PUT /carritos/{id}/confirmar`, `/cancelar-confirmacion`, `/completar-venta`, `DELETE /carritos/{id}` | Dueño del carrito o ADMINISTRADOR |
| | `GET /carritos` (todos) | ADMINISTRADOR |
| Ventas | `GET /ventas/{id}`, `GET /ventas?idUsuario=`, `PUT /ventas/{id}/cancelar`, `PUT /ventas/{id}/reembolso` | Dueño de la venta o ADMINISTRADOR |
| | `PUT /ventas/{id}/estado` (PREPARANDO → ENVIADO → ENTREGADO) | PROVEEDOR o ADMINISTRADOR |

**Flujo de punta a punta:** login como PROVEEDOR → `POST /libros` → login como CLIENTE → `POST /carritos` → `POST /carritos/{id}/items` → `PUT /carritos/{id}/confirmar` → `PUT /carritos/{id}/completar-venta` → login como PROVEEDOR → `PUT /ventas/{id}/estado` (dos veces hasta ENTREGADO) → login como CLIENTE → `PUT /ventas/{id}/reembolso`.

## Códigos de error

- **400:** datos inválidos o vacíos (`@Valid`, `CasillaVaciaException`, JSON ilegible, UUID mal formado, parámetro faltante).
- **401 / 403:** sin token o con rol sin permiso.
- **404:** el recurso no existe (`RecursoNoEncontradoException`, `NoSuchElementException`).
- **409:** `RecursoDuplicadoException`, `StockInsuficienteException` y `DataIntegrityViolationException`.
- **422:** regla de negocio rota (`ReglaDominioException`).
- **500:** error inesperado.

## Correcciones de la Entrega 1

| Observación que me dieron | Qué cambiamos | Dónde se ve (archivo o rama) |
| --- | --- | --- |
| Al completar la venta del carrito no se verificaba que hubiera stock disponible. | (1) El ítem del carrito guarda el stock conocido del libro y `Carrito` lo valida con `verificarStockDisponible()` tanto en `confirmarCarrito()` como en `completarVentaCarrito()` (regla rota → 422). (2) `VerificadorDeCarrito.actualizarStockItemsCarritoCreado` mantiene sincronizado ese stock en los carritos CREADO cuando cambia el stock del libro (`ActualizarLibro.actualizarStock`) o se completa una venta. (3) Al completar la venta, `CompletarVentaCarrito` llama a `Libro.descontarStock`, que valida contra el stock real y lanza `StockInsuficienteException` (409); como el caso de uso es `@Transactional`, nada se guarda a medias. | `Carrito.java`, `ItemCarrito.java`, `VerificadorDeCarrito.java`, `Libro.java`, `CompletarVentaCarrito.java`, `ActualizarLibro.java`. Pruebas: `CarritoTest`, `CompletarVentaCarritoTest` |

## Pruebas avanzadas

Ejecutamos `./gradlew test` y pasan las 603 pruebas en verde. Todas siguen el patrón AAA (preparar, ejecutar, verificar).

### Parte 0 · Pruebas del repositorio

**¿Por qué probar contra H2 y no con Mockito?** Con Mockito solo comprobábamos que se llamaba al repositorio, no que el SQL, las columnas y las restricciones funcionaran. Con H2 sí vemos que lo que guardamos es lo que recuperamos.

**¿Qué hace `@Transactional` en un test?** Ejecuta la prueba dentro de una transacción y al terminar hace rollback, así que los datos de una prueba no afectan a las otras.

**¿Qué es el mapper y por qué la prueba también lo comprueba?** Es la clase que convierte la entidad de dominio en entidad JPA y al revés (`Libro` ↔ `LibroEntity`). Al guardar y recuperar, la prueba también verifica que esa conversión no pierda datos.

Las pruebas que hicimos:
- **Guardar y recuperar:** `LibroRepositoryJpaTest` comprueba que el libro vuelve con su título, ISBN, precio (Value Object con monto y moneda), stock, categoría y estado intactos.
- **Paginación:** en `LibroRepositoryJpaTest` guardamos 5 libros que coinciden con la búsqueda, uno agotado y otro que no coincide. Pedimos páginas de 2 y comprobamos el total (5), las páginas (3) y el orden por título.

### Parte 1 · Probar un endpoint con seguridad (401, 403 y 200)

Elegimos `GET /api/carritos`, que solo puede usar el ADMINISTRADOR. `spring-security-test` nos deja probar rutas protegidas sin hacer login real, y el `jwt()` hace que la petición llegue como si ya trajera un token con el rol y el `uid` que le indiquemos.

En `SeguridadDuenoRecursoTest` hicimos las tres pruebas: sin token responde 401, con token de CLIENTE responde 403 y con token de ADMINISTRADOR responde 200.

Un `@WebMvcTest` solo levanta la capa web y no carga nuestra `SecurityFilterChain`, así que usamos `@SpringBootTest` con `@AutoConfigureMockMvc` para probar la seguridad real. Es más lento, pero así el resultado refleja nuestras reglas.

**Lo que más nos costó:** al agregar la seguridad, los tests de controller empezaron a fallar con 401 y 403 y no entendíamos por qué. Lo resolvimos con `@AutoConfigureMockMvc(addFilters = false)` en los `@WebMvcTest` y dejando la seguridad real para esta clase. También nos tocó escribir las autoridades sin el prefijo `ROLE_`, igual que en nuestro `JwtGrantedAuthoritiesConverter`.

### Parte 2 · `@DataJpaTest` contra `@SpringBootTest`

`@DataJpaTest` levanta solo la persistencia (entidades y repositorios JPA), sin controllers, casos de uso ni seguridad. `@SpringBootTest` levanta toda la aplicación. Por eso en `@DataJpaTest` tuvimos que traer nuestras clases con `@Import` (`LibroRepositoryJpa` y `LibroPersistenceMapper`), y en `@SpringBootTest` no hace falta.

Por defecto `@DataJpaTest` reemplaza la base configurada por una H2 en memoria, y `@AutoConfigureTestDatabase` controla ese cambio. Además hace rollback solo, mientras que en `@SpringBootTest` hay que poner `@Transactional`. A cambio, arranca bastante más rápido.

En Spring Boot 4 el soporte de pruebas se separó en módulos: `@DataJpaTest` ahora está en `org.springframework.boot.data.jpa.test.autoconfigure`. Lo resolvimos agregando en el `build.gradle` los starters `spring-boot-starter-data-jpa-test`, `spring-boot-starter-webmvc-test` y `spring-boot-starter-validation-test`, además de `spring-security-test`.

Nuestras pruebas con `@DataJpaTest` están en `LibroJpaDataRepositoryTest` (búsqueda por texto y paginación solo de disponibles) y `LibroFiltrosRepositoryTest`.

**Lo que más nos costó:** con el import viejo no compilaba y tardamos en encontrar el paquete nuevo. Después nos falló el test diciendo que no encontraba el bean del repositorio, y era porque faltaba el `@Import`.

### Parte 3 · Prueba de integración

Una prueba de integración recorre el sistema real (controller, caso de uso, repositorio y base de datos) sin simular nada. Una unitaria prueba una sola clase con mocks: es muy rápida, pero solo confirma que se llamó a otras clases, no que funcionen juntas.

Las de integración son más lentas, pero detectan errores que las unitarias no ven, como un DTO mal mapeado, una consulta que falla o una regla de seguridad mal escrita. Por eso hicimos pocas, para los flujos importantes, y dejamos los casos de borde para las unitarias.

`CarritoLibroIntegracionTest` registra un libro, crea un carrito, agrega el ítem y consulta el carrito comprobando el título, la cantidad y el total. También prueba que un libro eliminado da 404 y que no se puede eliminar un libro que está en un carrito PROCESANDO (422). No usa ningún `@MockitoBean`.

**Lo que más nos costó:** que las pruebas no se estorbaran entre sí. Lo logramos usando `@Transactional` y ISBN e ids únicos en cada una.

### Parte 4 · Más casos en el controller

**¿Cómo hacemos que un mock lance una excepción?** Con `when(mock.metodo(...)).thenThrow(...)`. Si el método es `void` usamos `doThrow(...).when(mock).metodo(...)`, como en `AutorControllerTest` y `UsuarioControllerTest`.

**¿Cómo convierte el `@RestControllerAdvice` cada excepción en un código HTTP?** Nuestro `GlobalExceptionHandler` tiene un `@ExceptionHandler` por tipo de excepción y arma una respuesta JSON con timestamp, status y mensaje. En un `@WebMvcTest` sí se carga porque es un `@ControllerAdvice`. Los códigos están en la sección "Códigos de error".

**¿Qué hace `verify(mock, never()).metodo(...)`?** Comprueba que ese método nunca se llamó. Con él demostramos que, cuando algo falla, no se sigue de largo.

En `LibroControllerErroresTest` hicimos la prueba de 404 (el libro no existe) y la de regla de negocio rota, que en nuestro proyecto responde 422 y no 400. En ambas verificamos con `never()` que no se llamó al mapper ni al caso de uso que no correspondía. Que `guardar` nunca se llama cuando algo falla lo comprobamos en los casos de uso, por ejemplo `RegistrarLibroUseCaseTest` y `CompletarVentaCarritoTest`.